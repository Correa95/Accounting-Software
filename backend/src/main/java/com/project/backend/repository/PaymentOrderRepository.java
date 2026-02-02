package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.Payment;
import com.project.backend.entity.PaymentOrder;
@Repository
public interface  PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    //  List<Payment> findByCompanyIdAndActiveTrue(Long companyId);
    // Optional<Payment> findByIdAndCompanyIdAndActiveTrue(Long paymentId, Long companyId);

    Optional<Payment>  findByStripePaymentIntentId(long paymentIntentId);
}
