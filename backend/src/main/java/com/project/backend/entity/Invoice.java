package com.project.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.project.backend.enums.InvoiceStatus;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "invoices")
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String invoiceNumber;

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @Column(nullable = false)
    private LocalDate invoiceDueDate;

    @Column(nullable = true)
    private LocalDateTime paidAt;

    @Column(nullable = false, length = 3)
    private String currency = "usd";

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal invoiceAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal outstandingBalance;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    // =========================================================
    // Status
    // =========================================================

    /**
     * Lifecycle:
     *   PENDING  →  PAID      (via applyPayment / webhook)
     *   PENDING  →  CANCELLED (via cancelPayment)
     *   PAID     →  REFUNDED  (via processRefund — full refund)
     *   PAID     →  PARTIAL_REFUND (via processRefund — partial)
     *   PAID     →  OVERDUE   (via scheduled job when past due date)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus invoiceStatus;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<JournalEntryLine> journalEntryLines;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payments;


    public void applyPayment(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        if (outstandingBalance == null) {
            outstandingBalance = invoiceAmount;
        }
        if (amount.compareTo(outstandingBalance) > 0) {
            throw new IllegalArgumentException(
                "Payment of " + amount + " exceeds outstanding balance of " + outstandingBalance);
        }
        outstandingBalance = outstandingBalance.subtract(amount);
        if (outstandingBalance.compareTo(BigDecimal.ZERO) == 0) {
            markAsPaid();
        }
    }

    public void markAsPaid() {
        this.invoiceStatus = InvoiceStatus.PAID;
        this.outstandingBalance = BigDecimal.ZERO;
        this.paidAt = LocalDateTime.now();
    }

    public void applyRefund(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Refund amount must be positive.");
        }

        if (refundedAmount == null) {
            refundedAmount = BigDecimal.ZERO;
        }

        BigDecimal maxRefundable = invoiceAmount.subtract(refundedAmount);
        if (amount.compareTo(maxRefundable) > 0) {
            throw new IllegalArgumentException(
                "Refund of " + amount + " exceeds refundable balance of " + maxRefundable);
        }

        refundedAmount = refundedAmount.add(amount);
        outstandingBalance = outstandingBalance.add(amount); 

        boolean isFullRefund = refundedAmount.compareTo(invoiceAmount) == 0;
        this.invoiceStatus = isFullRefund
                ? InvoiceStatus.REFUNDED
                : InvoiceStatus.PARTIAL_REFUND;

        if (isFullRefund) {
            this.paidAt = null; // payment is no longer valid
        }
    }

    /**
     * Cancel this invoice before any payment is collected.
     * Throws if the invoice has already been paid.
     */
    public void cancel() {
        if (this.invoiceStatus == InvoiceStatus.PAID) {
            throw new IllegalStateException(
                "Invoice " + invoiceNumber + " is already paid. Issue a refund instead.");
        }
        if (this.invoiceStatus == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException(
                "Invoice " + invoiceNumber + " is already cancelled.");
        }
        this.invoiceStatus = InvoiceStatus.CANCELLED;
        this.active = false;
    }

    /**
     * Convenience check — is there still money owed?
     */
    public boolean hasOutstandingBalance() {
        return outstandingBalance != null
                && outstandingBalance.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Convenience check — can this invoice accept a payment right now?
     */
    public boolean isPayable() {
        return (invoiceStatus == InvoiceStatus.PENDING
                || invoiceStatus == InvoiceStatus.OVERDUE
                || invoiceStatus == InvoiceStatus.PARTIAL_REFUND)
                && hasOutstandingBalance()
                && active;
    }

    // =========================================================
    // JPA lifecycle hooks
    // =========================================================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Default outstanding balance to the full invoice amount
        if (this.outstandingBalance == null) {
            this.outstandingBalance = this.invoiceAmount;
        }

        // Default refunded amount
        if (this.refundedAmount == null) {
            this.refundedAmount = BigDecimal.ZERO;
        }

        // Default status if not explicitly set
        if (this.invoiceStatus == null) {
            this.invoiceStatus = InvoiceStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}