package com.project.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.PaymentOrder;
import com.project.backend.service.PaymentOrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PaymentOrderService paymentOrderService;

    // Replace with your Stripe webhook secret
    private static final String STRIPE_WEBHOOK_SECRET = "whsec_XXXXXXXXXXXXXXXX";

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader,@RequestBody String payload) {
        Event event;
        try {
            // Verify webhook signature
            event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook signature verification failed");
        }

        // Only handle payment intents for now
        if ("payment_intent.succeeded".equals(event.getType()) ||
            "payment_intent.payment_failed".equals(event.getType()) ||
            "payment_intent.canceled".equals(event.getType()) ||
            "payment_intent.processing".equals(event.getType())) {

            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);

            if (paymentIntent != null) {
                String stripePaymentIntentId = paymentIntent.getId();

                // Idempotent handling
                PaymentOrder order = paymentOrderService
                        .findByStripePaymentIntentId(stripePaymentIntentId)
                        .orElse(null);

                if (order == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PaymentOrder not found");
                }

                switch (event.getType()) {
                    case "payment_intent.succeeded" ->
                        paymentOrderService.markSuccessful(stripePaymentIntentId);

                    case "payment_intent.payment_failed" ->
                        paymentOrderService.markFailed(stripePaymentIntentId,
                                paymentIntent.getLastPaymentError() != null
                                        ? paymentIntent.getLastPaymentError().getMessage()
                                        : "Unknown failure");

                    case "payment_intent.canceled" ->
                        paymentOrderService.markCanceled(stripePaymentIntentId);

                    case "payment_intent.processing" ->
                        paymentOrderService.markProcessing(stripePaymentIntentId);
                }
            }
        }

        return ResponseEntity.ok("Webhook handled");
    }
}
