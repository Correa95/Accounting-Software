package com.project.backend.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.project.backend.enums.AccountSubType;
import com.project.backend.enums.AccountType;

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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private AccountSubType accountSubType;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Optional — only set if this account is linked to a real bank account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_transaction_id")
    private BankTransaction bankTransaction;

    // Balance computed from journal entry lines — not stored
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<JournalEntryLine> journalEntryLines = new ArrayList<>();

    // Computed balance: sum of debits - sum of credits
    public BigDecimal getComputedBalance() {
        BigDecimal debits = journalEntryLines.stream()
                .filter(l -> l.getDebit() != null)
                .map(JournalEntryLine::getDebit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal credits = journalEntryLines.stream()
                .filter(l -> l.getCredit() != null)
                .map(JournalEntryLine::getCredit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return debits.subtract(credits);
    }
}