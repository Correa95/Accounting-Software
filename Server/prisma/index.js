const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();
module.exports = prisma;
package com.ltp.contacts.company;


public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String legalName;
    private String address;
    private String phone;
    private String email;
    private String taxId;

    @Embeded
    private String currencyCode;
    // private String fiscalYearEnd;

    private List<Account> accounts;
    private List<String> customers;

    private List<String> vendors;
    private List<String> products;

    private List<String> journalEntries;
    private List<String> invoices;

    private List<String> bills;
    private List<String> payments;

    private List<String> bankTransactions;
    private List<String> taxRates;

    private List<String> fiscalPeriod;
    


    
}
