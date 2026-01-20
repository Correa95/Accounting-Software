package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Vendor;

/**
 * Repository for Vendor persistence.
 *
 * This repository enforces:
 * - Multi-tenant data isolation (company-scoped queries)
 * - Soft delete behavior (active = true)
 * - Fast existence checks for business rule validation
 *
 * Designed for an accounting / accounts payable system.
 */
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    /**
     * Retrieves all active vendors for a given company.
     *
     * Used for:
     * - Vendor listing screens
     * - Dropdowns in bills / payments
     *
     * Soft-deleted vendors are excluded.
     */
    List<Vendor> findByCompanyIdAndActiveTrue(long companyId);

    /**
     * Retrieves a single active vendor by ID and company.
     *
     * Prevents:
     * - Cross-company data access
     * - Access to soft-deleted vendors
     *
     * Used by service layer for read, update, and deactivate operations.
     */
    Optional<Vendor> findByIdAndCompanyIdAndActiveTrue(long vendorId, long companyId);

    /**
     * Checks if a vendor number already exists for a company.
     *
     * Vendor numbers must be unique per company in accounting systems.
     * Used during vendor creation and updates to enforce business rules
     * before hitting database constraints.
     */
    boolean existsByCompanyIdAndVendorNumber(long companyId, Long vendorNumber);

    /**
     * Checks if an email is already associated with a vendor in a company.
     *
     * Prevents duplicate vendor records and billing confusion.
     * Used for validation at the service layer.
     */
    boolean existsByCompanyIdAndEmail(long companyId, String email);

    /**
     * Checks if a tax ID already exists for a company.
     *
     * Critical for:
     * - Regulatory compliance
     * - 1099 reporting
     * - Avoiding duplicate vendors
     */
    boolean existsByCompanyIdAndTaxId(long companyId, String taxId);

    /**
     * Retrieves an active vendor by vendor number.
     *
     * Vendor numbers are commonly used in:
     * - Accounts payable workflows
     * - Internal accounting references
     *
     * Soft-deleted vendors are excluded.
     */
    Optional<Vendor> findByCompanyIdAndVendorNumberAndActiveTrue(long companyId, Long vendorNumber);
}
