package com.project.backend.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.entity.Customer;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.PaymentOrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeService {
    private final PaymentOrderRepository paymentOrderRepository;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest){
        try { 
            log.info("Creating payment intent for customer:{}",paymentRequest.getCustomerEmail()); 
            // Create or retrieve customer in Stripe
            Customer customer = createOrRetrieveCustomer(
                paymentRequest.getCustomerEmail());
            // Convert amount to cent (Stripe uses smallest unit)
            long amountInCent = paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

            // Create Payment Intent parameters
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amountInCent)
            .setCurrency(paymentRequest.getCurrency().toLowerCase())
            .setDescription(paymentRequest.getDescription())
            .setAutomaticPaymentMethods(PaymentIntentCreateParams
            .AutomaticPaymentMethods.builder()
            .setEnabled(true)
            .build())
            .build();
            // Create the Payment Intent
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            // Save order to database
            PaymentOrder paymentOrder = PaymentOrder.builder()
            .customerEmail(paymentRequest.getCustomerEmail())
            .amount(paymentRequest.getAmount())
            .currency(paymentRequest.getCurrency())
            .description(paymentRequest.getDescription())
            .stripePaymentIntentId(paymentIntent.getId())
            .stripeCustomerId(customer.getId())
            .status(PaymentStatus.PENDING)
            .build();

            paymentOrder = paymentOrderRepository.save(paymentOrder);
            log.info("Payment intent created:{}",
                paymentIntent.getId());

            return PaymentResponse.builder()
            .orderId(paymentOrder.getId())
            .clientSecret(paymentIntent.getId())
            .amount(paymentRequest.getAmount())
            .currency(paymentRequest.getCurrency())
            .paymentStatus(PaymentStatus.PENDING)
            .message("Payment intent created successfully")
            .build();
        } catch (StripeException e) {
            
            log.error("Error creating payment intent: {}",
                e.getMessage(), e);
                throw new PaymentException("Failed to create payment intent: " + e.getMessage(), e);
        }
    }

    private Customer createOrRetrievCustomer(String email)
        throws StripeException{
            customerCreateParams params = CustomerCreateParams.builder().setEmail(email).build();
            return Customer.create(params);
        } 

    private PaymentStatus mapStripeStatus(String stripeStatus){
        return switch (stripeStatus){
        case "required_payment_method", "required_confirmation","required_action" -> PaymentStatus.PENDING;
        case "processing" -> PaymentStatus.PROCESSING;
        case "successful" -> PaymentStatus.SUCCESSFUL;
        case "canceled" -> PaymentStatus.CANCELED;
        default -> PaymentStatus.FAILED;
        };
    }

    @Transactional
    public PaymentResponse cancelPayment(String paymentIntentId){
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            PaymentIntent canceledIntent = paymentIntent.cancel();
            PaymentOrder paymentOrder = paymentOrderRepository
            .findByStripePaymentIntentId(paymentIntentId)
            .orElseThrow(()-> new PaymentException("Payment order not found"));

            PaymentOrder.setPaymentStatus(PaymentStatus.CANCELED);
            PaymentOrderRepository.save(paymentOrder);
            return buildResponse(paymentOrder, canceledIntent);
        } catch (StripeException e) {
            throw new PaymentException("Failed to cancel payment: " + e.getMessage(), e);
        };
    }
}
