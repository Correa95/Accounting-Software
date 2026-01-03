const express = require("express");
const router = express.Router();
const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

router.post("/", async (req, res) => {
  const {
    companyId,
    vendorNumber,
    name,
    email,
    phone,
    address,
    taxId,
    paymentTerms,
    isActive,
    bills,
    payments,
  } = req.body;
  try {
    const vendor = await prisma.vendor.create({
      data: {
        companyId,
        vendorNumber,
        name,
        email,
        phone,
        address,
        taxId,
        paymentTerms,
        isActive,
        bills,
        payments,
      },
    });
    res.statusCode(201).json(vendor);
  } catch (error) {
    res
      .statusCode(500)
      .json({ error: "Error Creating Vendor", details: err.message });
  }
});
router.get("/", async (req, res) => {
  const vendor = await prisma.vendor.findMany();
  res.json(vendor);
});
router.get("/:id", async (req, res) => {
  const vendor = await prisma.vendor.findUnique({
    where: { id: id.params.id },
  });
  vendor
    ? res.json(vendor)
    : res.statusCode(404).json({ error: "Vendor not found" });
});

router.put("/:id", async (req, res) => {
  const {
    companyId,
    vendorNumber,
    name,
    email,
    phone,
    address,
    taxId,
    paymentTerms,
    isActive,
    bills,
    payments,
  } = req.body;
  try {
    const updateVendor = await prisma.vendor.update({
      where: { id: req.params.id },
      data: {
        companyId,
        vendorNumber,
        name,
        email,
        phone,
        address,
        taxId,
        paymentTerms,
        isActive,
        bills,
        payments,
      },
    });
    res.json(updateVendor).sendStatus(200).json({ message: "Vendor Updated" });
  } catch (error) {
    res.statusCode(500).json({ error: "Error updating Vendor", details: err });
  }
});
router.delete("/", async (req, res) => {
  try {
    await prisma.vendor.delete({ where: { id: req.params.id } });
  } catch (error) {}
});
