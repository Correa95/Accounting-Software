const express = require("express");
const router = express.Router();
const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

router.post("/", async (req, res) => {
  const {
    companyId,
    accountNumber,
    accountName,
    accountTypeId,
    parentAccountId,
    childAccounts,
    description,
    isActive,
    isSystemAccount,
    ProductsIncome,
    ProductsExpense,
    billLines,
    bankTransactions,
    paymentsBank,
  } = req.body;
  try {
    const account = await prisma.account.create({
      data: {
        companyId,
        accountNumber,
        accountName,
        accountTypeId,
        parentAccountId,
        childAccounts,
        description,
        isActive,
        isSystemAccount,
        ProductsIncome,
        ProductsExpense,
        billLines,
        bankTransactions,
        paymentsBank,
      },
    });
    res.json(account).statusCode(201);
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error creating company", details: error.message });
  }
});
router.get("/", async (req, res) => {
  const account = await prisma.account.findMany();
  res.json(account);
});

router.get("/:id", async (req, res) => {
  const account = await prisma.account.findUnique({
    where: { id: req.params.id },
  });
  account
    ? res.json(account)
    : res.statusCode(404).json({ error: "Account not found" });
});

router.put("/:id", async (req, res) => {
  const {
    companyId,
    accountNumber,
    accountName,
    accountTypeId,
    parentAccountId,
    childAccounts,
    description,
    isActive,
    isSystemAccount,
    ProductsIncome,
    ProductsExpense,
    billLines,
    bankTransactions,
    paymentsBank,
  } = req.body;
  try {
    const updateAccount = await prisma.account.Update({
      where: { id: req.params.id },
      data: {
        companyId,
        accountNumber,
        accountName,
        accountTypeId,
        parentAccountId,
        childAccounts,
        description,
        isActive,
        isSystemAccount,
        ProductsIncome,
        ProductsExpense,
        billLines,
        bankTransactions,
        paymentsBank,
      },
    });
    res
      .json(updateAccount)
      .sendStatus(200)
      .json({ message: "Account updated" });
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error updating Account", details: error });
  }
});
router.delete("/:id", async (req, res) => {
  try {
    await prisma.account.delete({ where: { id: req.params.id } });
    res.json({ message: "Account Deleted" });
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error Deleting Account", details: error });
  }
});
module.exports = router;
