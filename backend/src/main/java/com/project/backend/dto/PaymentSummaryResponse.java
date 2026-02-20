package com.project.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.backend.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lightweight payment view for list endpoints:
 *   GET /api/payments/by-invoice/{invoiceId}
 *   GET /api/payments/by-customer/{customerId}
 *   GET /api/payments/by-status
 *   GET /api/payments/by-currency
 *
 * Use this instead of PaymentResponse when returning many records
 * to keep the payload small. Does not include clientSecret,
 * failureReason, or audit timestamps that are only needed on detail views.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSummaryResponse {

    private Long id;
    private String stripePaymentIntentId;
    private BigDecimal amount;
    private BigDecimal refundedAmount;
    private String currency;
    private PaymentStatus paymentStatus;
    private String invoiceNumber;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
}