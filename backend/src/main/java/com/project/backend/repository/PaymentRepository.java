
   
package com.project.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.Payment;
import com.project.backend.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Used by the Stripe webhook handler to look up the payment
     * and its invoice in a single query (avoids N+1).
     */
    @Query("SELECT p FROM Payment p LEFT JOIN FETCH p.invoice WHERE p.stripePaymentIntentId = :intentId")
    Optional<Payment> findByStripePaymentIntentIdWithInvoice(@Param("intentId") String intentId);

    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);

    /**
     * Find the active (non-failed, non-cancelled) payment for an invoice.
     * Used by initiatePayment() to detect and reuse an existing PENDING intent
     * rather than creating a duplicate charge.
     */
    @Query("""
            SELECT p FROM Payment p
            WHERE p.invoice.invoiceNumber = :invoiceNumber
              AND p.paymentStatus = :status
            ORDER BY p.createdAt DESC
            LIMIT 1
            """)
    Optional<Payment> findLatestByInvoiceNumberAndStatus(
            @Param("invoiceNumber") String invoiceNumber,
            @Param("status") PaymentStatus status);

    /**
     * Find the completed payment for an invoice — used when initiating a refund.
     */
    @Query("""
            SELECT p FROM Payment p
            WHERE p.invoice.invoiceNumber = :invoiceNumber
              AND p.paymentStatus IN (
                  com.project.backend.enums.PaymentStatus.COMPLETED,
                  com.project.backend.enums.PaymentStatus.PARTIAL_REFUND
              )
            ORDER BY p.processedAt DESC
            LIMIT 1
            """)
    Optional<Payment> findCompletedPaymentByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);
}
}
