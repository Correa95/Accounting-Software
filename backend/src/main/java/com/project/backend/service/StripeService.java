package com.project.backend.service;

import org.springframework.transaction.annotation.Transactional;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.repository.PaymentOrderRepository;

public class StripeService {
    private final PaymentOrderRepository paymentOrderRepository;
    @Transactional
    public PaymentResponse createPaymentIntent(PaymentRequest paymentRequest){
        try {
            log.info("Creating payment intent for customer: {}",
                paymentRequest.getCustomerEmail());

            // Creating or retrieve customer in Stripe
            Customer customer = createOrRetrieveCustomer(
                paymentRequest.getCustomerEmail());
        } catch (Exception e) {
            
        }
    }

    // PaymentResponse createPayment(PaymentRequest paymentRequest);

    // void handleWebhook(String payload, String sigHeader);
}
