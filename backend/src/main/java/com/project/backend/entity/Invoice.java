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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus invoiceStatus;

    @Column(nullable=false)
    private LocalDateTime paidAt;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal invoiceAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal outstandingBalance;


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
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<JournalEntryLine> journalEntryLines;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private PaymentOrder payments;

    // === Lifecycle ===

//     public void markAsPaid() {
//         this.invoiceStatus = InvoiceStatus.PAID;
//         this.outstandingBalance = BigDecimal.ZERO;
//     }

//     public void applyPayment(BigDecimal amount) {
//     if (paidAmount == null) {
//         paidAmount = BigDecimal.ZERO;
//     }

//     paidAmount = paidAmount.add(amount);
//     outstandingBalance = totalAmount.subtract(paidAmount);

//     if (outstandingBalance.compareTo(BigDecimal.ZERO) <= 0) {
//         markAsPaid();
//     }
// }

// ==========================
// Domain behavior
// ==========================

    public void applyPayment(BigDecimal amount) {
    if (amount == null || amount.signum() <= 0) {
        throw new IllegalArgumentException("Payment amount must be positive");
    }

    if (outstandingBalance == null) {
        outstandingBalance = invoiceAmount;
    }

    if (amount.compareTo(outstandingBalance) > 0) {
        throw new IllegalArgumentException("Payment exceeds outstanding balance");
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
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.outstandingBalance == null) {
            this.outstandingBalance = this.invoiceAmount;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
