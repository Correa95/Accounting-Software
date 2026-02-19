package com.project.backend.service;

import java.util.List;
import java.util.Optional;

import com.project.backend.entity.Payment;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public interface PaymentService {

    public PatmentIntent createPayment(CreatePaymentRequest request) throws Exception{
        PaymentIntentCreateParams params = PaymentIntentCreateParams.Builder()
        .setAmount(request.getAmount())
        .setCurrency(request.getCurrency())
        .setDescription(request.getDescription())
        .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods
            .builder()
            .setEnabled(true)
            .build())
            .build();
            return PaymentIntent.create(params);
    }  
}














// List<Payment> getAllPayment(long invoiceId);
//     Payment getPayment(long invoice, long companyId);
    
//     Payment makePayment(Payment paymentOrder);

    
//     Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);

