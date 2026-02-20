package com.project.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.backend.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    // =========================================================
    // Identity
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // Stripe references
    // =========================================================

    /**
     * Stripe PaymentIntent ID — e.g. "pi_3Oxxxxxxxxxxxxxxxx".
     * Created when the customer initiates checkout.
     * Used to cancel, retrieve, or look up the intent on Stripe.
     * Unique because each PaymentIntent maps to exactly one payment attempt.
     */
    @Column(unique = true, length = 100)
    private String stripePaymentIntentId;

    /**
     * Stripe Refund ID — e.g. "re_3Oxxxxxxxxxxxxxxxx".
     * Populated when a refund is issued via the Stripe API.
     * For audit purposes — cross-reference with Stripe Dashboard.
     */
    @Column(unique = true, length = 100)
    private String stripeRefundId;

    // =========================================================
    // Financials
    // =========================================================

    /**
     * The amount charged to the customer.
     * Mirrors Invoice.outstandingBalance at the time of payment initiation.
     * Stored in the unit of {@code currency} (e.g. dollars, not cents).
     */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    /**
     * Running total of amounts refunded on this payment record.
     * Starts at zero; incremented by PaymentService.processRefund().
     * Guards against over-refunding: refundedAmount must never exceed amount.
     */
    @Column(nullable = false, precision = 19, scale = 4)
    @Builder.Default
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    /**
     * ISO 4217 currency code — e.g. "usd", "eur", "gbp".
     * Must match Invoice.currency and is passed directly to Stripe.
     */
    @Column(nullable = false, length = 3)
    private String currency;

    /**
     * Human-readable description shown on the Stripe Dashboard
     * and on the customer's bank statement (where supported).
     * e.g. "Payment for Invoice INV-1001"
     */
    @Column(nullable = false)
    private String description;

    // =========================================================
    // Status
    // =========================================================

    /**
     * Lifecycle:
     *   PENDING        — PaymentIntent created, customer has not yet paid
     *   COMPLETED      — Stripe webhook confirmed payment_intent.succeeded
     *   CANCELLED      — Cancelled before capture (PENDING → CANCELLED)
     *   REFUNDED       — Full refund issued (COMPLETED → REFUNDED)
     *   PARTIAL_REFUND — Partial refund issued (COMPLETED → PARTIAL_REFUND)
     *   FAILED         — Stripe reported payment_intent.payment_failed
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    /**
     * Reason provided by Stripe when a payment fails.
     * e.g. "Your card has insufficient funds."
     * Null for non-failed payments.
     */
    @Column(length = 500)
    private String failureReason;

    // =========================================================
    // Timestamps
    // =========================================================

    /**
     * When the PaymentIntent was created (payment initiated).
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last time this record was modified (status change, refund, etc.)
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * When Stripe confirmed the payment was successfully processed.
     * Set by handlePaymentSuccess() via the webhook.
     * Null until the payment completes.
     */
    @Column(nullable = true)
    private LocalDateTime processedAt;

    /**
     * When the full payment lifecycle ended — either fully refunded
     * or confirmed complete with no further action expected.
     * Null while the payment is still active.
     */
    @Column(nullable = true)
    private LocalDateTime completedAt;

    // =========================================================
    // Relationships
    // =========================================================

    /**
     * The invoice this payment is for.
     * ManyToOne because multiple payment attempts can exist per invoice
     * (e.g. first attempt failed, second succeeded).
     * Only one payment per invoice should ever reach COMPLETED status.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    /**
     * The customer making the payment.
     * Denormalised here for fast lookup without joining through Invoice.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // =========================================================
    // Domain behaviour
    // =========================================================

    /**
     * Mark this payment as successfully completed.
     * Called by PaymentService.handlePaymentSuccess() after the
     * Stripe webhook confirms payment_intent.succeeded.
     */
    public void markCompleted() {
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.processedAt = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
        this.failureReason = null;
    }

    /**
     * Mark this payment as failed and store the reason from Stripe.
     * Called by PaymentService.handlePaymentFailed() via webhook.
     *
     * @param reason the failure message from Stripe (e.g. "Insufficient funds")
     */
    public void markFailed(String reason) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    /**
     * Mark this payment as cancelled.
     * Called by PaymentService.cancelPayment() after Stripe confirms cancellation.
     */
    public void markCancelled() {
        this.paymentStatus = PaymentStatus.CANCELLED;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * Apply a refund amount to this payment record.
     * Updates refundedAmount and sets status to REFUNDED or PARTIAL_REFUND.
     * Called by PaymentService.processRefund() after Stripe confirms the refund.
     *
     * @param refundAmount   the amount being refunded (dollars/euros, not cents)
     * @param stripeRefundId the refund ID returned by Stripe
     * @throws IllegalArgumentException if refund exceeds the refundable balance
     */
    public void applyRefund(BigDecimal refundAmount, String stripeRefundId) {
        if (refundAmount == null || refundAmount.signum() <= 0) {
            throw new IllegalArgumentException("Refund amount must be positive.");
        }

        BigDecimal alreadyRefunded = this.refundedAmount != null
                ? this.refundedAmount : BigDecimal.ZERO;
        BigDecimal refundable = this.amount.subtract(alreadyRefunded);

        if (refundAmount.compareTo(refundable) > 0) {
            throw new IllegalArgumentException(
                "Refund of " + refundAmount + " exceeds refundable balance of " + refundable);
        }

        this.refundedAmount = alreadyRefunded.add(refundAmount);
        this.stripeRefundId = stripeRefundId;

        boolean isFullRefund = this.refundedAmount.compareTo(this.amount) == 0;
        this.paymentStatus = isFullRefund
                ? PaymentStatus.REFUNDED
                : PaymentStatus.PARTIAL_REFUND;

        if (isFullRefund) {
            this.completedAt = LocalDateTime.now();
        }
    }

    /**
     * How much can still be refunded on this payment.
     * Convenience method used by PaymentService before calling Stripe.
     */
    public BigDecimal getRefundableBalance() {
        BigDecimal alreadyRefunded = this.refundedAmount != null
                ? this.refundedAmount : BigDecimal.ZERO;
        return this.amount.subtract(alreadyRefunded);
    }

    /**
     * Whether this payment can still be cancelled on Stripe.
     * A PaymentIntent can only be cancelled while it has not been captured.
     */
    public boolean isCancellable() {
        return this.paymentStatus == PaymentStatus.PENDING;
    }

    /**
     * Whether a refund can be issued against this payment.
     */
    public boolean isRefundable() {
        return this.paymentStatus == PaymentStatus.COMPLETED
                || this.paymentStatus == PaymentStatus.PARTIAL_REFUND;
    }

    // =========================================================
    // JPA lifecycle hooks
    // =========================================================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (this.refundedAmount == null) {
            this.refundedAmount = BigDecimal.ZERO;
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}