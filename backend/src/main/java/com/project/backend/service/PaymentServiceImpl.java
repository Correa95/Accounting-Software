package com.project.backend.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.config.StripeConfig;
import com.project.backend.dto.CancelRefundResponse;
import com.project.backend.dto.InvoiceSummaryResponse;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.entity.Invoice;
import com.project.backend.entity.Payment;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.InvoiceRepository;
import com.project.backend.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final StripeConfig stripeConfig;

    @Override
    @Transactional(readOnly = true)
    public InvoiceSummaryResponse getInvoiceSummary(String invoiceNumber) {
        Invoice invoice = findInvoice(invoiceNumber);
        return new InvoiceSummaryResponse(
                invoice.getInvoiceNumber(),
                invoice.getCustomer().getName(),
                invoice.getInvoiceAmount(),
                invoice.getOutstandingBalance(),
                invoice.getCurrency(),
                invoice.getInvoiceStatus().name(),
                invoice.getInvoiceDueDate().toString()
        );
    }

    // INITIATE — create Stripe PaymentIntent
    /**
     * Creates a Stripe PaymentIntent for the given invoice number.
     *
     * Idempotent — if a PENDING Payment already exists for this invoice,
     * the existing clientSecret is returned to prevent duplicate charges.
     *
     * Persists a Payment record linked to the Invoice and Customer
     * so the webhook can update both when Stripe confirms payment.
     */
    @Override
    public PaymentResponse initiatePayment(String invoiceNumber) throws StripeException {
        Invoice invoice = findInvoice(invoiceNumber);

        // Guard: only payable invoices may proceed
        if (!invoice.isPayable()) {
            throw new IllegalStateException(
                "Invoice " + invoiceNumber + " cannot be paid. Current status: "
                + invoice.getInvoiceStatus());
        }

        // Idempotency: reuse an existing PENDING intent rather than double-charging
        var existingPending = paymentRepository
                .findLatestByInvoiceNumberAndStatus(invoiceNumber, PaymentStatus.PENDING);

        if (existingPending.isPresent()) {
            Payment existing = existingPending.get();
            log.info("Reusing PENDING PaymentIntent {} for invoice {}",
                    existing.getStripePaymentIntentId(), invoiceNumber);
            PaymentIntent intent = PaymentIntent.retrieve(existing.getStripePaymentIntentId());
            return buildIntentResponse(invoice, intent.getClientSecret());
        }

        // Build Stripe metadata for traceability in the Dashboard
        Map<String, String> metadata = new HashMap<>();
        metadata.put("invoice_number", invoice.getInvoiceNumber());
        metadata.put("customer_id", invoice.getCustomer().getId().toString());
        metadata.put("company_id", invoice.getCompany().getId().toString());

        // Create PaymentIntent on Stripe
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(invoice.getOutstandingBalance()
                        .multiply(java.math.BigDecimal.valueOf(100))
                        .setScale(0, java.math.RoundingMode.HALF_UP)
                        .longValueExact())
                .setCurrency(invoice.getCurrency())
                .setDescription("Payment for Invoice: " + invoiceNumber)
                .putAllMetadata(metadata)
                .build();

        PaymentIntent intent = PaymentIntent.create(
                params,
                com.stripe.net.RequestOptions.builder()
                        // Prevents duplicate intents if the HTTP request is retried
                        .setIdempotencyKey("initiate-" + invoiceNumber)
                        .build()
        );

        log.info("Created Stripe PaymentIntent {} for invoice {}", intent.getId(), invoiceNumber);

        // Persist Payment record — @Builder pattern matches the entity
        Payment payment = Payment.builder()
                .stripePaymentIntentId(intent.getId())
                .amount(invoice.getOutstandingBalance())
                .refundedAmount(BigDecimal.ZERO)
                .currency(invoice.getCurrency())
                .description("Payment for Invoice: " + invoiceNumber)
                .paymentStatus(PaymentStatus.PENDING)
                .invoice(invoice)
                .customer(invoice.getCustomer())
                .build();

        paymentRepository.save(payment);

        return buildIntentResponse(invoice, intent.getClientSecret());
    }

    // =========================================================
    // CANCEL — cancel a PENDING payment
    // =========================================================

    /**
     * Cancels the Stripe PaymentIntent and marks the Payment as CANCELLED.
     * Also cancels the Invoice via Invoice.cancel().
     *
     * Only valid while the payment is still PENDING (not yet captured by Stripe).
     * If payment is already COMPLETED, throw — use refund instead.
     */
    @Override
    public CancelRefundResponse cancelPayment(String invoiceNumber) throws StripeException {
        Invoice invoice = findInvoice(invoiceNumber);

        Payment payment = paymentRepository
                .findLatestByInvoiceNumberAndStatus(invoiceNumber, PaymentStatus.PENDING)
                .orElseThrow(() -> new IllegalStateException(
                    "No pending payment found for invoice: " + invoiceNumber
                    + ". If the invoice is already paid, use refund instead."));

        if (!payment.isCancellable()) {
            throw new IllegalStateException(
                "Payment cannot be cancelled. Current status: " + payment.getPaymentStatus());
        }

        // Cancel on Stripe
        PaymentIntent intent = PaymentIntent.retrieve(payment.getStripePaymentIntentId());
        intent.cancel();

        log.info("Cancelled Stripe PaymentIntent {} for invoice {}", intent.getId(), invoiceNumber);

        // Update entities using domain methods
        payment.markCancelled();
        invoice.cancel();

        paymentRepository.save(payment);
        invoiceRepository.save(invoice);

        return new CancelRefundResponse(
                "Payment cancelled successfully.",
                invoiceNumber,
                payment.getStripePaymentIntentId(),
                PaymentStatus.CANCELLED.name(),
                null,
                null
        );
    }

    // =========================================================
    // REFUND — full refund
    // =========================================================

    @Override
    public CancelRefundResponse refundPayment(String invoiceNumber) throws StripeException {
        return processRefund(invoiceNumber, null);
    }

    // =========================================================
    // REFUND — partial refund
    // =========================================================

    @Override
    public CancelRefundResponse partialRefundPayment(String invoiceNumber,
                                                      BigDecimal refundAmount) throws StripeException {
        if (refundAmount == null || refundAmount.signum() <= 0) {
            throw new IllegalArgumentException("Refund amount must be a positive value.");
        }
        return processRefund(invoiceNumber, refundAmount);
    }

    // =========================================================
    // WEBHOOK — payment succeeded
    // =========================================================

    /**
     * Called by StripeWebhookHandler after Stripe fires payment_intent.succeeded.
     *
     * Uses Payment.markCompleted() and Invoice.applyPayment() domain methods
     * to update both records in one transaction. The invoice's applyPayment()
     * automatically calls markAsPaid() when outstanding balance reaches zero.
     */
    @Override
    public void handlePaymentSuccess(String paymentIntentId) {
        paymentRepository
                .findByStripePaymentIntentIdWithInvoice(paymentIntentId)
                .ifPresentOrElse(payment -> {
                    payment.markCompleted();
                    paymentRepository.save(payment);

                    Invoice invoice = payment.getInvoice();
                    if (invoice != null) {
                        invoice.applyPayment(payment.getAmount());
                        invoiceRepository.save(invoice);
                        log.info("Invoice {} marked PAID via webhook", invoice.getInvoiceNumber());
                    }
                }, () -> log.warn(
                    "Webhook: no Payment found for PaymentIntent {}", paymentIntentId));
    }

    // =========================================================
    // WEBHOOK — payment failed
    // =========================================================

    /**
     * Called by StripeWebhookHandler after Stripe fires payment_intent.payment_failed.
     *
     * Stores the failure reason from Stripe on the Payment record.
     * The invoice status is left unchanged so the customer can retry.
     */
    @Override
    public void handlePaymentFailed(String paymentIntentId, String failureMessage) {
        paymentRepository
                .findByStripePaymentIntentId(paymentIntentId)
                .ifPresentOrElse(payment -> {
                    payment.markFailed(failureMessage);
                    paymentRepository.save(payment);
                    log.warn("Payment failed for PaymentIntent {}: {}",
                            paymentIntentId, failureMessage);
                }, () -> log.warn(
                    "Webhook: no Payment found for failed PaymentIntent {}", paymentIntentId));
    }

    // =========================================================
    // QUERIES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentByIntentId(String stripePaymentIntentId) {
        return paymentRepository
                .findByStripePaymentIntentId(stripePaymentIntentId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Payment not found for intent: " + stripePaymentIntentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCustomer(Long customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCustomerAndStatus(Long customerId, PaymentStatus status) {
        return paymentRepository.findByCustomerIdAndPaymentStatus(customerId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCurrency(String currency) {
        return paymentRepository.findByCurrency(currency);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCurrencyAndStatus(String currency, PaymentStatus status) {
        return paymentRepository.findByCurrencyAndPaymentStatus(currency, status);
    }

    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    private CancelRefundResponse processRefund(String invoiceNumber,
                                                BigDecimal refundAmount) throws StripeException {
        Invoice invoice = findInvoice(invoiceNumber);

        // Find the completed payment to refund against
        Payment payment = paymentRepository
                .findCompletedPaymentByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new IllegalStateException(
                    "No completed payment found for invoice: " + invoiceNumber));

        if (!payment.isRefundable()) {
            throw new IllegalStateException(
                "Payment cannot be refunded. Current status: " + payment.getPaymentStatus());
        }

        // null refundAmount means full refund of remaining balance
        BigDecimal amountToRefund = (refundAmount == null)
                ? payment.getRefundableBalance()
                : refundAmount;

        // Pre-check before hitting Stripe to give a clean error message
        if (amountToRefund.compareTo(payment.getRefundableBalance()) > 0) {
            throw new IllegalArgumentException(
                "Refund of " + amountToRefund
                + " exceeds refundable balance of " + payment.getRefundableBalance());
        }

        // Issue refund on Stripe
        RefundCreateParams refundParams = RefundCreateParams.builder()
                .setPaymentIntent(payment.getStripePaymentIntentId())
                .setAmount(amountToRefund
                        .multiply(java.math.BigDecimal.valueOf(100))
                        .setScale(0, java.math.RoundingMode.HALF_UP)
                        .longValueExact())
                .build();

        Refund refund = Refund.create(refundParams);

        log.info("Stripe refund {} for invoice {} — amount: {}",
                refund.getId(), invoiceNumber, amountToRefund);

        // Update Payment via domain method (handles status + refundedAmount)
        payment.applyRefund(amountToRefund, refund.getId());
        paymentRepository.save(payment);

        // Update Invoice via domain method (handles status + outstandingBalance)
        invoice.applyRefund(amountToRefund);
        invoiceRepository.save(invoice);

        boolean isFullRefund = payment.getPaymentStatus() == PaymentStatus.REFUNDED;

        return new CancelRefundResponse(
                isFullRefund
                        ? "Full refund processed successfully."
                        : "Partial refund processed successfully.",
                invoiceNumber,
                payment.getStripePaymentIntentId(),
                payment.getPaymentStatus().name(),
                refund.getId(),
                java.math.BigDecimal.valueOf(refund.getAmount())
                        .divide(java.math.BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP)
        );
    }

    private Invoice findInvoice(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumberWithPayment(invoiceNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Invoice not found: " + invoiceNumber));
    }

    private PaymentResponse buildIntentResponse(Invoice invoice, String clientSecret) {
        return PaymentResponse.builder()
                .clientSecret(clientSecret)
                .invoiceNumber(invoice.getInvoiceNumber())
                .outstandingBalance(invoice.getOutstandingBalance())
                .amount(invoice.getInvoiceAmount())
                .customerName(invoice.getCustomer().getName())
                .currency(invoice.getCurrency())
                .build();
    }
}
