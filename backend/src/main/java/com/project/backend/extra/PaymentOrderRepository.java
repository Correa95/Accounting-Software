package com.project.backend.extra;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.entity.Payment;

public interface  PaymentOrderRepository extends JpaRepository<Payment, Long>{
    Payment findByPayment(long invoiceId, long customerId, PaymentRequest paymentRequest);
}
