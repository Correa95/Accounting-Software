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
import jakarta.persistence.Index;
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
@Table(
    name = "payment_orders",
    indexes = {
        @Index(name = "idx_payment_intent", columnList = "stripe_payment_intent_id"),
        @Index(name = "idx_payment_status", columnList = "payment_status"),
        @Index(name = "idx_invoice_id", columnList = "invoice_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Business customer (source of truth) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /** Amount paid */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    /** ISO currency code (USD, EUR, etc.) */
    @Column(nullable = false, length = 3)
    private String currency;

    /** Human-readable description */
    @Column(nullable = false)
    private String description;

    /** Stripe PaymentIntent ID (external reference) */
    @Column(nullable = false, unique = true)
    private String stripePaymentIntentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    /** Invoice being settled */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    /** When Stripe begins processing */
    private LocalDateTime processedAt;

    /** Final state timestamp (success / fail / cancel) */
    private LocalDateTime completedAt;

    /** Failure reason if payment fails */
    @Column(length = 500)
    private String failureReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
