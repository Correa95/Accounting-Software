package com.project.backend.dto;

import java.util.List;

import com.project.backend.entity.Payment;

/**
 * Converts Payment entities to their DTO representations.
 *
 * Static utility — no Spring bean needed since there are no dependencies.
 *
 * Usage:
 *   PaymentResponse dto     = PaymentMapper.toResponse(payment);
 *   PaymentSummaryResponse  = PaymentMapper.toSummary(payment);
 *   List<PaymentSummaryResponse> = PaymentMapper.toSummaryList(payments);
 */
public final class PaymentMapper {

    private PaymentMapper() {}

    // ── Full detail (single record endpoints) ─────────────────────────────────

    /**
     * Maps to full PaymentResponse — used after initiatePayment()
     * so the frontend gets the clientSecret alongside all payment details.
     *
     * @param payment    the saved Payment entity
     * @param clientSecret the Stripe PaymentIntent clientSecret
     */
    public static PaymentResponse toResponse(Payment payment, String clientSecret) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .clientSecret(clientSecret)
                .stripePaymentIntentId(payment.getStripePaymentIntentId())
                .amount(payment.getAmount())
                .refundedAmount(payment.getRefundedAmount())
                .refundableBalance(payment.getRefundableBalance())
                .currency(payment.getCurrency())
                .paymentStatus(payment.getPaymentStatus())
                .description(payment.getDescription())
                .failureReason(payment.getFailureReason())
                .createdAt(payment.getCreatedAt())
                .processedAt(payment.getProcessedAt())
                .completedAt(payment.getCompletedAt())
                .invoiceId(payment.getInvoice() != null ? payment.getInvoice().getId() : null)
                .invoiceNumber(payment.getInvoice() != null ? payment.getInvoice().getInvoiceNumber() : null)
                .customerId(payment.getCustomer() != null ? payment.getCustomer().getId() : null)
                .customerName(payment.getCustomer() != null ? payment.getCustomer().getName() : null)
                .build();
    }

    /**
     * Maps to PaymentResponse without a clientSecret.
     * Used for detail lookup endpoints (GET /api/payments/intent/{intentId})
     * where the payment already exists — clientSecret is not needed again.
     */
    public static PaymentResponse toResponse(Payment payment) {
        return toResponse(payment, null);
    }

    // ── Lightweight summary (list endpoints) ──────────────────────────────────

    public static PaymentSummaryResponse toSummary(Payment payment) {
        return PaymentSummaryResponse.builder()
                .id(payment.getId())
                .stripePaymentIntentId(payment.getStripePaymentIntentId())
                .amount(payment.getAmount())
                .refundedAmount(payment.getRefundedAmount())
                .currency(payment.getCurrency())
                .paymentStatus(payment.getPaymentStatus())
                .invoiceNumber(payment.getInvoice() != null ? payment.getInvoice().getInvoiceNumber() : null)
                .customerName(payment.getCustomer() != null ? payment.getCustomer().getName() : null)
                .createdAt(payment.getCreatedAt())
                .processedAt(payment.getProcessedAt())
                .build();
    }

    // ── List helpers ──────────────────────────────────────────────────────────

    public static List<PaymentResponse> toResponseList(List<Payment> payments) {
        return payments.stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    public static List<PaymentSummaryResponse> toSummaryList(List<Payment> payments) {
        return payments.stream()
                .map(PaymentMapper::toSummary)
                .toList();
    }
}