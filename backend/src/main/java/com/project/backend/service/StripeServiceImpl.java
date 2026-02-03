package com.project.backend.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.service.JournalService;
import com.project.backend.service.StripeService;
import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private final JournalService journalService;

    @PostConstruct
    void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        PaymentIntentCreateParams params =
            PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                .setCurrency(request.getCurrency())
                .setReceiptEmail(request.getCustomerEmail())
                .build();

        try {
            PaymentIntent intent = PaymentIntent.create(params);

            return PaymentResponse.builder()
                .paymentIntentId(intent.getId())
                .clientSecret(intent.getClientSecret())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .paymentStatus(PaymentStatus.PENDING)
                .message("Payment intent created")
                .build();

        } catch (Exception e) {
            throw new RuntimeException("Stripe payment failed", e);
        }
    }

    @Override
    public void handleWebhook(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElseThrow();

                // 🔑 Accounting happens here
                journalService.recordStripePayment(intent);
            }

        } catch (Exception e) {
            throw new RuntimeException("Webhook processing failed", e);
        }
    }
}
