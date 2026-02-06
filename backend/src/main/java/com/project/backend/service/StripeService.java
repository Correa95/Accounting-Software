package com.project.backend.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.entity.Customer;
import com.project.backend.entity.Invoice;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;

// import com.project.backend.exception.RuntimeException;
import com.project.backend.repository.CustomerRepository;
import com.project.backend.repository.InvoiceRepository;
import com.project.backend.repository.PaymentOrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
@Builder
@Service
@RequiredArgsConstructor
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentOrderRepository paymentOrderRepository;

    @Transactional
    public PaymentResponse createPaymentIntent(PaymentRequest request) {
        try {
            // 1️⃣ Load customer
            Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));

            // 2️⃣ Load invoice
            Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));

            // Make sure customer do not overpay
            if (request.getAmount().compareTo(invoice.getOutstandingBalance()) > 0) {
                throw new RuntimeException("Payment exceeds outstanding invoice balance");
            }

            // 3️⃣ Create Stripe customer if missing
            if (customer.getStripeCustomerId() == null) {
                com.stripe.model.Customer stripeCustomer = com.stripe.model.Customer.create(
                CustomerCreateParams.builder().setEmail(customer.getEmail()).build());

                customer.setStripeCustomerId(stripeCustomer.getId());
                customerRepository.save(customer);
            }

            // 4️⃣ Amount in cents
        long amountInCents = request.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

            // 5️⃣ Create Stripe PaymentIntent
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(request.getCurrency().toLowerCase())
                .setCustomer(customer.getStripeCustomerId())
                .setDescription(request.getDescription())
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // 6️⃣ Save PaymentOrder
            PaymentOrder paymentOrder = PaymentOrder.builder()
                    .customer(customer)
                    .invoice(invoice)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .description(request.getDescription())
                    .stripePaymentIntentId(paymentIntent.getId())
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();

            paymentOrderRepository.save(paymentOrder);

            // 7️⃣ Return response
            return PaymentResponse.builder()
                    .invoiceId(invoice.getId())
                    .paymentIntentId(paymentIntent.getId())
                    .clientSecret(paymentIntent.getClientSecret())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .paymentStatus(PaymentStatus.PENDING)
                    .message("Payment intent created successfully")
                    .build();

        } catch (StripeException e) {
            log.error("Stripe error", e);
            throw new RuntimeException("Failed to create payment intent", e);
        }
    }

    public PaymentStatus mapStripePaymentStatus(String stripeStatus) {
        return switch (stripeStatus) {
            case "requires_payment_method",
                 "requires_confirmation",
                 "requires_action" -> PaymentStatus.PENDING;
            case "processing" -> PaymentStatus.PROCESSING;
            case "succeeded" -> PaymentStatus.SUCCESSFUL;
            case "canceled" -> PaymentStatus.CANCELED;
            default -> PaymentStatus.FAILED;
        };
    }
}
