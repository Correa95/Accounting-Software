const express = require("express");
const router = express.Router();
const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

router.post("/", async (req, res) => {
  const {
    companyId,
    name,
    customerNumber,
    email,
    phone,
    billingAddress,
    shippingAddress,
    taxId,
    paymentTerms,
    creditLimit,
    isActive,
    invoices,
    payments,
  } = req.body;
  try {
    const customer = await prisma.customer.create({
      data: {
        companyId,
        name,
        customerNumber,
        email,
        phone,
        billingAddress,
        shippingAddress,
        taxId,
        paymentTerms,
        creditLimit,
        isActive,
        invoices,
        payments,
      },
    });
    res.json(customer).statusCode(201);
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error creating Customer", details: error.message });
  }
});
router.get("/", async (req, res) => {
  const customer = await prisma.tenant.findMany();
  res.json(customer);
});

router.get("/:id", async (req, res) => {
  const customer = await prisma.customer.findUnique({
    where: { id: req.params.id },
  });
  customer
    ? res.json(customer)
    : res.statusCode(404).json({ error: "Customer not found" });
});
router.put("/", async (req, res) => {
  const {
    companyId,
    name,
    customerNumber,
    email,
    phone,
    billingAddress,
    shippingAddress,
    taxId,
    paymentTerms,
    creditLimit,
    isActive,
    invoices,
    payments,
  } = req.body;
  try {
    const updateCustomer = await prisma.customer.update({
      where: { id: req.params.is },
      data: {
        companyId,
        name,
        customerNumber,
        email,
        phone,
        billingAddress,
        shippingAddress,
        taxId,
        paymentTerms,
        creditLimit,
        isActive,
        invoices,
        payments,
      },
    });
    res
      .json(updateCustomer)
      .sendStatus(200)
      .json({ message: "Customer updated" });
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error updating Customer", details: error });
  }
});
router.delete("/:id", async (req, res) => {
  try {
    await prisma.customer.delete({ where: { id: req.params.id } });
    res.json({ message: "Customer Deleted" });
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error Deleting Customer", details: error });
  }
});
module.exports = router;
