package com.project.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent by the admin to issue a partial refund.
 * POST /api/payments/refund/partial
 *
 * refundAmount is in dollars/euros (BigDecimal) — NOT cents.
 * The service converts to cents before calling Stripe.
 *
 * For a full refund use POST /api/payments/refund/full?invoiceNumber=INV-1001
 * — no request body needed.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartialRefundRequest {

    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    @NotNull(message = "Refund amount is required")
    @DecimalMin(value = "0.01", message = "Refund amount must be at least 0.01")
    private BigDecimal refundAmount;
}