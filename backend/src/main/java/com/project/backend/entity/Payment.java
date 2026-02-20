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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String stripePaymentIntentId;

    @Column(unique = true, length = 100)
    private String stripeRefundId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 4)
    @Builder.Default
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(length = 500)
    private String failureReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private LocalDateTime processedAt;

    @Column(nullable = true)
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    public void markCompleted() {
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.processedAt = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
        this.failureReason = null;
    }

    public void markFailed(String reason) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    public void markCancelled() {
        this.paymentStatus = PaymentStatus.CANCELLED;
        this.completedAt = LocalDateTime.now();
    }

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

    public BigDecimal getRefundableBalance() {
        BigDecimal alreadyRefunded = this.refundedAmount != null
                ? this.refundedAmount : BigDecimal.ZERO;
        return this.amount.subtract(alreadyRefunded);
    }

    public boolean isCancellable() {
        return this.paymentStatus == PaymentStatus.PENDING;
    }

    public boolean isRefundable() {
        return this.paymentStatus == PaymentStatus.COMPLETED
                || this.paymentStatus == PaymentStatus.PARTIAL_REFUND;
    }

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