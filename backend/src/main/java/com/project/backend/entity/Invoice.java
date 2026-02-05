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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@Entity
@Table(
    name = "invoices",
    indexes = {
        @Index(name = "idx_invoice_number", columnList = "invoice_number"),
        @Index(name = "idx_invoice_company", columnList = "company_id"),
        @Index(name = "idx_invoice_customer", columnList = "customer_id")
    }
)

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

    private LocalDateTime paidAt;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal invoiceAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal outstandingBalance;


    @Column(nullable = false)
    private boolean active = true;

    // === Audit ===
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // === Relationships ===

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /** Accounts Receivable account */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    /** Accounting is read-only from invoice side */
    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<JournalEntryLine> journalEntryLines;

    /** Current Stripe payment (can evolve to OneToMany later) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_order_id")
    private PaymentOrder paymentOrder;

    // === Lifecycle ===

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;

        if (this.remainingAmount == null) {
            this.remainingAmount = this.invoiceAmount;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
