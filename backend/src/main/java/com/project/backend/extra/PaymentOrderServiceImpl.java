package com.project.backend.extra;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.entity.Customer;
// import com.project.backend.entity.Invoice;
// import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
// import com.project.backend.repository.InvoiceRepository;
// import com.project.backend.service.JournalEntryService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentOrderServiceImpl implements PaymentOrderService {

    // private final PaymentOrderRepository paymentOrderRepository;
    // private final InvoiceRepository invoiceRepository;
    // private final JournalEntryService journalEntryService;


    private final PaymentOrderRepository paymentOrderRepository;

    @Transactional
    public PaymentResponse createPaymentIntent(PaymentRequest request) {
        try {
            log.info("Creating payment intent for customer: {}",
                request.getCustomerEmail());

            // Create or retrieve customer in Stripe
            Customer customer = createOrRetrieveCustomer(
                request.getCustomerEmail()
            );

            // Convert amount to cents (Stripe uses smallest unit)
            long amountInCents = request.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValue();

            // Create Payment Intent parameters
            PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(request.getCurrency().toLowerCase())
                    .setCustomer(customer.getId())
                    .setDescription(request.getDescription())
                    .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams
                            .AutomaticPaymentMethods.builder()
                            .setEnabled(true)
                            .build()
                    )
                    .build();

            // Create the Payment Intent
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Save order to database
            PaymentOrder paymentOrder = PaymentOrder.builder()
                .customerEmail(request.getCustomerEmail())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .stripePaymentIntentId(paymentIntent.getId())
                .stripeCustomerId(customer.getId())
                .status(PaymentStatus.PENDING)
                .build();

            paymentOrder = paymentOrderRepository.save(paymentOrder);

            log.info("Payment intent created: {}",
                paymentIntent.getId());

            return PaymentResponse.builder()
                .orderId(paymentOrder.getId())
                .paymentIntentId(paymentIntent.getId())
                .clientSecret(paymentIntent.getClientSecret())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.PENDING)
                .message("Payment intent created successfully")
                .build();

        } catch (StripeException e) {
            log.error("Error creating payment intent: {}",
                e.getMessage(), e);
            throw new PaymentException(
                "Failed to create payment intent: " + e.getMessage(), e
            );
        }
    }

    private Customer createOrRetrieveCustomer(String email)
            throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
            .setEmail(email)
            .build();

        return Customer.create(params);
    }

    // private PaymentStatus mapStripeStatus(String stripeStatus) {
    private PaymentStatus mapStripeStatus(String stripeStatus) {
        return switch (stripeStatus) {
            case "requires_payment_method",
                 "requires_confirmation",
                 "requires_action" -> PaymentStatus.PENDING;
            case "processing" -> PaymentStatus.PROCESSING;
            case "succeeded" -> PaymentStatus.SUCCEEDED;
            case "canceled" -> PaymentStatus.CANCELED;
            default -> PaymentStatus.FAILED;
        };
    }
}

    

