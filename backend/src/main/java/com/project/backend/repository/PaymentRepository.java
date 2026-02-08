package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentOrder, Long> {

   

    // Customer payment history
    List<PaymentOrder> findByCustomerId(Long customerId);

    List<PaymentOrder> findByInvoiceId(long invoiceId);

    // Accounting & reporting
    List<PaymentOrder> findByPaymentStatus(PaymentStatus paymentStatus);

    // Invoice settlement (if linked)
    Optional<PaymentOrder> findByInvoiceId(Long invoiceId);

    // Reconciliation / audits
    List<PaymentOrder> findByCurrency(String currency);
}
