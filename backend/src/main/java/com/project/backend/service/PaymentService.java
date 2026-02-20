package com.project.backend.service;

import java.math.BigDecimal;
import java.util.List;

import com.project.backend.dto.CancelRefundResponse;
import com.project.backend.dto.InvoiceSummaryResponse;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.entity.Payment;
import com.project.backend.enums.PaymentStatus;
import com.stripe.exception.StripeException;

public interface PaymentService {

    // ── Invoice lookup (before payment) ──────────────────────────────────────
    InvoiceSummaryResponse getInvoiceSummary(String invoiceNumber);

    // ── Stripe payment flow ───────────────────────────────────────────────────
    PaymentResponse initiatePayment(String invoiceNumber) throws StripeException;

    CancelRefundResponse cancelPayment(String invoiceNumber) throws StripeException;

    CancelRefundResponse refundPayment(String invoiceNumber) throws StripeException;

    CancelRefundResponse partialRefundPayment(String invoiceNumber,
                                               BigDecimal refundAmount) throws StripeException;

    // ── Webhook handlers ──────────────────────────────────────────────────────
    void handlePaymentSuccess(String paymentIntentId);

    void handlePaymentFailed(String paymentIntentId, String failureMessage);

    // ── Queries ───────────────────────────────────────────────────────────────
    Payment getPaymentByIntentId(String stripePaymentIntentId);

    List<Payment> getPaymentsByInvoice(Long invoiceId);

    List<Payment> getPaymentsByCustomer(Long customerId);

    List<Payment> getPaymentsByCustomerAndStatus(Long customerId, PaymentStatus status);

    List<Payment> getPaymentsByStatus(PaymentStatus status);

    List<Payment> getPaymentsByCurrency(String currency);

    List<Payment> getPaymentsByCurrencyAndStatus(String currency, PaymentStatus status);
}