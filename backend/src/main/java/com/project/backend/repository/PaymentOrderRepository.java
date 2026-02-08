package com.project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.entity.PaymentOrder;

public interface  PaymentOrderRepository extends JpaRepository<PaymentOrder, Long>{
    PaymentOrder findByPayment(long invoiceId, long customerId, PaymentRequest paymentRequest);
}
