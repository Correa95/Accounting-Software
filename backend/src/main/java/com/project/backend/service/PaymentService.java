package com.project.backend.service;

import org.springframework.stereotype.Service;

import com.project.backend.dto.PaymentRequest;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public interface PaymentService {
    // public PaymentIntent createPayment(CreatePaymentRequest request) throws Exception{
    public PaymentIntent createPayment(PaymentRequest paymentRequest)throws Exception{
        PaymentIntentCreateParams params = PaymentIntentCreateParams.Builder()
        .setAmount(paymentRequest.getAmount())
        .setCurrency(paymentRequest.getCurrency())
        .setDescription(paymentRequest.getDescription())
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

