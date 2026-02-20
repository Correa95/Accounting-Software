package com.project.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.project.backend.enums.TransactionType;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank_transactions")
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private LocalDate transactionDate;
    
    @Column(name="transaction_amount", nullable=false)
    private BigDecimal transactionAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; 
    
    @Column(nullable=false)
    private String description;
    
    @Column(unique = true, nullable = false)
    private String referenceNumber;
    
    @Column(nullable=false)
    private boolean active = true;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "bank_account_id", nullable = false)
    // private BankAccount bankAccount; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry; 



}
