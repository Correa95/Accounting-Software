package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.Invoice;
import com.project.backend.entity.Payment;
import com.project.backend.enums.InvoiceStatus;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // =========================================================
    // Core lookups (your originals — kept exactly)
    // =========================================================

    List<Invoice> findByCompanyIdAndActiveTrue(Long companyId);

    Optional<Invoice> findByIdAndCompanyIdAndActiveTrue(Long invoiceId, Long companyId);

    // =========================================================
    // BUG FIX — was returning Optional<Payment>, must be Optional<Invoice>
    // Used by InvoiceServiceImpl.markInvoicePaid() to locate the
    // invoice linked to a Payment via the @OneToOne payments field.
    // =========================================================

    @Query("SELECT i FROM Invoice i WHERE i.payments = :payment")
    Optional<Invoice> findByPayment(@Param("payment") Payment payment);

    // =========================================================
    // Invoice number lookup
    // Used by PaymentService.initiatePayment() and getInvoiceSummary()
    // to find an invoice from the number the customer types in.
    // =========================================================

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    /**
     * Eagerly fetches the linked Payment in the same query.
     * Avoids N+1 when PaymentService needs to check for an
     * existing PENDING payment before creating a new PaymentIntent.
     */
    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.payments WHERE i.invoiceNumber = :invoiceNumber")
    Optional<Invoice> findByInvoiceNumberWithPayment(@Param("invoiceNumber") String invoiceNumber);

    boolean existsByInvoiceNumber(String invoiceNumber);

    // =========================================================
    // Status filtering
    // Used by InvoiceServiceImpl.getInvoicesByStatus() and
    // scheduled jobs (e.g. marking overdue invoices).
    // =========================================================

    List<Invoice> findByCompanyIdAndInvoiceStatusAndActiveTrue(
            Long companyId, InvoiceStatus invoiceStatus);

    /**
     * Find all active invoices for a company with a given status
     * and a due date before today — used by a scheduled job to
     * mark SENT invoices as OVERDUE when their due date passes.
     */
    @Query("""
            SELECT i FROM Invoice i
            WHERE i.company.id = :companyId
              AND i.invoiceStatus = :status
              AND i.invoiceDueDate < CURRENT_DATE
              AND i.active = true
            """)
    List<Invoice> findOverdueInvoices(
            @Param("companyId") Long companyId,
            @Param("status") InvoiceStatus status);

    // =========================================================
    // Customer invoice history
    // Used on the customer profile page to show all invoices
    // raised against a specific customer.
    // =========================================================

    List<Invoice> findByCustomerIdAndActiveTrue(Long customerId);

    List<Invoice> findByCustomerIdAndInvoiceStatusAndActiveTrue(
            Long customerId, InvoiceStatus invoiceStatus);
}