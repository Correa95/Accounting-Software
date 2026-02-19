package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.Payment;
import com.project.backend.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

   

    // Customer payment history
    List<Payment> findByCustomerId(Long customerId);

    List<Payment> findByInvoiceId(long invoiceId);

    // Accounting & reporting
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    // Invoice settlement (if linked)
    Optional<Payment> findByInvoiceId(Long invoiceId);

    // Reconciliation / audits
    List<Payment> findByCurrency(String currency);
}
