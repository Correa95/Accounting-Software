package com.project.backend.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name="legal_name", nullable = false)
    private String legalName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name="tax_id", nullable = false)
    private String taxId;

    @Column(name="tax_rate", nullable = false)
    private String taxRate;
    
    @Column(name="currency_code", nullable = false)
    private String currencyCode;

    @Column(name="fiscal_year_start", nullable = false)
    private LocalDate fiscalYearStart;
    
    @Column(name="fiscal_year_end", nullable = false)
    private LocalDate fiscalYearEnd;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Account> accounts;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Customer> customers;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Vendor> vendors;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Product> products;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<JournalEntry> journalEntries;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Bill> bills;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Payment> payments;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<BankTransaction> bankTransactions;



    
}
