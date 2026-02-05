package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    // Stripe → Webhook lookups (CRITICAL)
    Optional<PaymentOrder> findByStripePaymentIntentId(String stripePaymentIntentId);

    // Customer payment history
    List<PaymentOrder> findByCustomer_Id(Long customerId);

    // Accounting & reporting
    List<PaymentOrder> findByPaymentStatus(PaymentStatus status);

    // Invoice settlement (if linked)
    Optional<PaymentOrder> findByInvoiceId(Long invoiceId);

    // Reconciliation / audits
    List<PaymentOrder> findByCurrency(String currency);
}
