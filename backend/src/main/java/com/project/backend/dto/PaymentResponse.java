package com.project.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.backend.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Returned after a successful PaymentIntent creation (POST /api/payments/initiate).
 *
 * The clientSecret is passed directly to Stripe.js on the frontend
 * to render the PaymentElement and confirm the charge.
 *
 * Also includes invoice and customer details so the frontend can display
 * a payment summary alongside the card form without a separate API call.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    // ── Stripe ────────────────────────────────────────────────────────────────
    private Long id;

    /**
     * Passed to Stripe.js — used to confirm the payment on the frontend.
     * Never log or expose this beyond the immediate API response.
     */
    private String clientSecret;

    /**
     * Stripe PaymentIntent ID (pi_xxxxx).
     * Safe to store and display — not sensitive.
     */
    private String stripePaymentIntentId;

    // ── Financials ────────────────────────────────────────────────────────────
    private BigDecimal amount;
    private BigDecimal refundedAmount;
    private BigDecimal refundableBalance;
    private String currency;

    // ── Status ────────────────────────────────────────────────────────────────
    private PaymentStatus paymentStatus;
    private String description;
    private String failureReason;

    // ── Timestamps ────────────────────────────────────────────────────────────
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private LocalDateTime completedAt;

    // ── Related entities (IDs + display names only — no nested objects) ───────
    private Long invoiceId;
    private String invoiceNumber;
    private Long customerId;
    private String customerName;
}