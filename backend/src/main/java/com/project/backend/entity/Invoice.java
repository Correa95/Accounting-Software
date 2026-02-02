package com.project.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.project.backend.enums.InvoiceStatus;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "invoices",
    indexes = {
        @Index(name = "idx_invoice_number", columnList = "invoiceNumber"),
        @Index(name = "idx_invoice_company", columnList = "company_id"),
        @Index(name = "idx_invoice_customer", columnList = "customer_id")
    }
)

public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
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
    private BigDecimal remainingAmount;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal invoiceAmount; 

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id") // Accounts Receivable account
    private Account account;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntryLine> journalEntryLines;

    @OneToOne
    @JoinColumn(name = "payment_order_id")
    private PaymentOrder paymentOrder;

}
