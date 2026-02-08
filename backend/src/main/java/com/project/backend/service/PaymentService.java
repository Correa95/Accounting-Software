package com.project.backend.service;

import java.util.List;
import java.util.Optional;

import com.project.backend.entity.PaymentOrder;

public interface PaymentService {

    List<PaymentOrder> getAllPayment(long invoiceId);
    PaymentOrder getPayment(long invoice, long companyId);
    
    PaymentOrder makePayment(PaymentOrder paymentOrder);

    
    Optional<PaymentOrder> findByStripePaymentIntentId(String stripePaymentIntentId);
}

