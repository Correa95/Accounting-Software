package com.project.backend.service;

import java.util.List;
import java.util.Optional;

import com.project.backend.entity.Payment;

public interface PaymentService {

    List<Payment> getAllPayment(long invoiceId);
    Payment getPayment(long invoice, long companyId);
    
    Payment makePayment(Payment paymentOrder);

    
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
}

