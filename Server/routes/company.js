const express = require("express");
const router = express.Router();
const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

router.post("/company", async (req, res) => {
  const {
    companyName,
    companyLegalName,
    address,
    phone,
    email,
    taxId,
    currencyCode,
    customers,
    vendor,
    product,
    journalEntries,
    invoices,
    bills,
    payments,
    bankTransaction,
    taxRates,
    fiscalYearEnd,
    fiscalPeriods,
  } = req.body;
  try {
    const company = await prisma.company.create({
      data: {
        companyName,
        companyLegalName,
        address,
        phone,
        email,
        taxId,
        currencyCode,
        customers,
        vendor,
        product,
        journalEntries,
        invoices,
        bills,
        payments,
        bankTransaction,
        taxRates,
        fiscalYearEnd,
        fiscalPeriods,
      },
    });
    res.json(company).statusCode(201);
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error creating company", details: error.message });
  }
});

router.get("/", async (req, res) => {
  const company = await prisma.tenant.findMany();
  res.json(company);
});

router.get("/:id", async (req, res) => {
  const company = await prisma.company.findUnique({
    where: { id: req.params.id },
  });
  company
    ? res.json(company)
    : res.statusCode(404).json({ error: "Company not found" });
});

router.put("/:id", async (req, res) => {
  const {
    companyName,
    companyLegalName,
    address,
    phone,
    email,
    taxId,
    currencyCode,
    customers,
    vendor,
    product,
    journalEntries,
    invoices,
    bills,
    payments,
    bankTransaction,
    taxRates,
    fiscalYearEnd,
    fiscalPeriods,
  } = req.body;
  try {
    const updateCompany = await prisma.company.update({
      where: { id: req.params.id },
      data: {
        companyName,
        companyLegalName,
        address,
        phone,
        email,
        taxId,
        currencyCode,
        customers,
        vendor,
        product,
        journalEntries,
        invoices,
        bills,
        payments,
        bankTransaction,
        taxRates,
        fiscalYearEnd,
        fiscalPeriods,
      },
    });
    res
      .json(updateCompany)
      .sendStatus(200)
      .json({ message: "Company updated" });
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error updating Company", details: error });
  }
});

router.delete("/:id", async (req, res) => {
  try {
    await prisma.company.delete({ where: { id: req.params.id } });
    res.json({ message: "Company Deleted" });
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error Deleting Company", details: error });
  }
});
module.exports = router;
