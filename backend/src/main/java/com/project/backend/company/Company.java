package com.project.backend.company;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Company {
    @Id
    private String id;
    private String name;
    private String legalName;
    private String address;
    private String phone;
    private String email;
    private String taxId;

    private String currencyCode;

    private  FiscalPeriod fiscalPeriod;

    private List<Account> accounts = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    private List<Vendor> vendors = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    private List<JournalEntry> journalEntries = new ArrayList<>();
    private List<Invoice> invoices = new ArrayList<>();

    private List<Bill> bills = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    private List<BankTransaction> bankTransactions = new ArrayList<>();
    private List<TaxRate> taxRates = new ArrayList<>();



    public String getId() {
    return id;
    }

    public void setId(String id) {
    this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalName() {
        return this.legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaxId() {
        return this.taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public FiscalPeriod getFiscalPeriod() {
        return this.fiscalPeriod;
    }

    public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
        this.fiscalPeriod = fiscalPeriod;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Vendor> getVendors() {
        return this.vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<JournalEntry> getJournalEntries() {
        return this.journalEntries;
    }

    public void setJournalEntries(List<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
    }

    public List<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<Bill> getBills() {
        return this.bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<BankTransaction> getBankTransactions() {
        return this.bankTransactions;
    }

    public void setBankTransactions(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public List<TaxRate> getTaxRates() {
        return this.taxRates;
    }

    public void setTaxRates(List<TaxRate> taxRates) {
        this.taxRates = taxRates;
    }
 
    
}


        
