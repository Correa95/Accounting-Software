package com.project.backend.service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import com.stripe.model.PaymentIntent;
import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.entity.Customer;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.PaymentOrderRepository;
import com.stripe.param.PaymentIntentCreateParams;


public class StripeService {
    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentOrder paymentOrder;
    @Transactional
    public PaymentResponse createPaymentIntent(PaymentRequest paymentRequest){
        try {
            log.info("Creating payment intent for customer: {}",
                paymentRequest.getCustomerEmail());

            // Creating or retrieve customer in Stripe
             Customer customer = createOrRetrieveCustomer(
                paymentRequest.getCustomerEmail()
            );

            // Convert amount to cents (Stripe uses smallest unit)
            long amountInCents = paymentRequest.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValue();

              // Create Payment Intent parameters
            PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(paymentRequest.getCurrency().toLowerCase())
                .setCustomer(customer.getId())
                .setInvoice(paymentRequest.getInvoiceNumber())
                .build();

            // Create the payment Intent
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Save order to database
            PaymentOrder paymentOrder = PaymentOrder.builder()
            .customerEmail(paymentRequest.getCustomerEmail())
            .amount(paymentRequest.getAmount())
            .currency(paymentRequest.getCurrency())
            .stripePaymentIntentId(paymentIntent.getId())
            .stripeCustomerId(customer.getId())
            .paymentStatus(PaymentStatus.PENDING);

        } catch (Exception e) {
            
        }
    }

}
