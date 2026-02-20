package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.Payment;
import com.project.backend.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p LEFT JOIN FETCH p.invoice WHERE p.stripePaymentIntentId = :intentId")
    Optional<Payment> findByStripePaymentIntentIdWithInvoice(@Param("intentId") String intentId);

    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);

    List<Payment> findByInvoiceId(Long invoiceId);

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

    List<Payment> findByCustomerId(Long customerId);

    List<Payment> findByCustomerIdAndPaymentStatus(Long customerId, PaymentStatus paymentStatus);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findByCurrency(String currency);

    List<Payment> findByCurrencyAndPaymentStatus(String currency, PaymentStatus paymentStatus);
}