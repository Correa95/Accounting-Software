package com.project.backend.company;

import java.util.List;

public class Company {
 
    private String name;
    private String legalName;
    private String address;
    private String phone;
    private String email;
    private String taxId;

    private String currencyCode;
    @Embedded
    private  FiscalPeriod fiscalPeriod;

    @OneToMany(mappedBy = "company") //One company → many accounts
    private List<Account> accounts;

    @OneToMany(mappedBy = "company")
    private List<Customer> customers;
    
    @OneToMany(mappedBy = "company")
    private List<Vendor> vendors;

    @OneToMany(mappedBy = "company")
    private List<Product> products;
    
    @OneToMany(mappedBy = "company")
    private List<JournalEntry> journalEntries;

    @OneToMany(mappedBy = "company")
    private List<Invoice> invoices;
    
    @OneToMany(mappedBy = "company")
    private List<Bill> bills;

    @OneToMany(mappedBy = "company")
    private List<Payment> payments;
    
    @OneToMany(mappedBy = "company")
    private List<BankTransaction> bankTransactions;

    @OneToMany(mappedBy = "company")
    private List<TaxRate> taxRates;



 
    
}


        
