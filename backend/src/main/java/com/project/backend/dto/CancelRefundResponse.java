package com.project.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Returned from:
 *   POST /api/payments/cancel
 *   POST /api/payments/refund/full
 *   POST /api/payments/refund/partial
 *
 * refundId and amountRefunded are null for cancel operations.
 * stripePaymentIntentId is always present for traceability.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelRefundResponse {

    private String message;
    private String invoiceNumber;
    private String stripePaymentIntentId;
    private String paymentStatus;

    /**
     * Stripe Refund ID (re_xxxxx).
     * Null for cancel operations — only populated when a refund is issued.
     */
    private String refundId;

    /**
     * The amount refunded in dollars/euros (not cents).
     * Null for cancel operations.
     */
    private BigDecimal amountRefunded;
}