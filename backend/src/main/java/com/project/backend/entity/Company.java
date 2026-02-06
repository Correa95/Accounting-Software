package com.project.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.project.backend.enums.BusinessType;
import com.project.backend.extra.Payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable=false)
    private boolean active = true;

    @Column(name="tax_id", nullable = false)
    private String taxId;

    @Column(name="tax_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate;
    
    @Column(name="currency_code", nullable = false)
    private String currencyCode;

    @Column(name="fiscal_year_start", nullable = false)
    private LocalDate fiscalYearStart;
    
    @Column(name="fiscal_year_end", nullable = false)
    private LocalDate fiscalYearEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessType businessType;

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
    private List<Bank> banks;



    
}
