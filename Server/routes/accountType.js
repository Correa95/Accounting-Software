const express = require("express");
const router = express.Router();
const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

router.post("/", async (req, res) => {
  const { name, category, normalBalance, accounts } = req.body;
  try {
    const accountType = await prisma.accountType.create({
      name,
      category,
      normalBalance,
      accounts,
    });
    res.json(accountType).statusCode(201);
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error creating Account Type", details: error.message });
  }
});

router.get("/", async (req, res) => {
  const accountType = await prisma.accountType.findMany();
  res.json(accountType);
});

router.get("/:id", async (req, res) => {
  const accountType = await prisma.accountType.findUnique({
    where: { id: req.params.id },
  });
  accountType
    ? res.json(accountType)
    : res.sendStatus(404).json({ error: "Account Type Not Found" });
});

router.put("/:id", async (req, res) => {
  const { name, category, normalBalance, accounts } = req.body;
  try {
    const updateAccountType = await prisma.accountType.update({
      where: { id: req.params.id },
      data: { name, category, normalBalance, accounts },
    });
    res
      .json(updateAccountType)
      .sendStatus(200)
      .json({ message: "Account Type Updated " });
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error updating Account Type", details: error });
  }
});

router.delete("/:id", async (req, res) => {
  try {
    await prisma.company.delete({ where: { id: req.params.id } });
    res.json({ message: "Account Type Deleted" });
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error Deleting Account Type", details: error });
  }
});
