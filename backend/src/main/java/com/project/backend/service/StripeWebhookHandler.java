package com.project.backend.service;

import org.springframework.stereotype.Component;

import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Routes Stripe webhook events to the appropriate PaymentService methods.
 *
 * Extracted from PaymentController to keep the controller thin and
 * make it easy to add new event types without touching HTTP-layer code.
 *
 * Stripe event reference:
 *   https://stripe.com/docs/api/events/types
 *
 * Events handled:
 *   payment_intent.succeeded       → handlePaymentSuccess()
 *   payment_intent.payment_failed  → handlePaymentFailed()
 *   payment_intent.canceled        → logged only (cancelled via our API)
 *   charge.refunded                → logged only (refund via our API)
 *
 * Events intentionally NOT handled here (Stripe fires them but we
 * initiate these actions ourselves, so our DB is already up to date):
 *   payment_intent.created         → we create it, we know about it
 *   charge.succeeded               → covered by payment_intent.succeeded
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StripeWebhookHandler {

    private final PaymentService paymentService;

    /**
     * Entry point — called by PaymentController after signature verification.
     *
     * @param event the verified Stripe Event object
     */
    public void handle(Event event) {
        log.info("Processing Stripe event: {} [id={}]", event.getType(), event.getId());

        switch (event.getType()) {

            case "payment_intent.succeeded" ->
                handlePaymentSucceeded(event);

            case "payment_intent.payment_failed" ->
                handlePaymentFailed(event);

            case "payment_intent.canceled" ->
                handlePaymentCanceled(event);

            case "charge.refunded" ->
                handleChargeRefunded(event);

            default ->
                log.debug("Unhandled Stripe event type: {}", event.getType());
        }
    }

    // =========================================================
    // EVENT HANDLERS
    // =========================================================

    private void handlePaymentSucceeded(Event event) {
        PaymentIntent intent = deserializePaymentIntent(event);
        if (intent == null) return;

        log.info("Payment succeeded — PaymentIntent: {}", intent.getId());
        paymentService.handlePaymentSuccess(intent.getId());
    }

    private void handlePaymentFailed(Event event) {
        PaymentIntent intent = deserializePaymentIntent(event);
        if (intent == null) return;

        // Extract the human-readable failure reason from Stripe
        String failureMessage = "Payment failed.";
        if (intent.getLastPaymentError() != null) {
            failureMessage = intent.getLastPaymentError().getMessage();
            log.warn("Payment failed — PaymentIntent: {} | Reason: {} | Code: {}",
                    intent.getId(),
                    failureMessage,
                    intent.getLastPaymentError().getCode());
        }

        paymentService.handlePaymentFailed(intent.getId(), failureMessage);
    }

    private void handlePaymentCanceled(Event event) {
        // We cancel via our own API so our DB is already updated.
        // Log for audit trail but no further action needed.
        PaymentIntent intent = deserializePaymentIntent(event);
        if (intent != null) {
            log.info("Payment canceled confirmed by Stripe — PaymentIntent: {}", intent.getId());
        }
    }

    private void handleChargeRefunded(Event event) {
        // We issue refunds via our own API so our DB is already updated.
        // Log the Stripe Refund ID for cross-reference.
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isPresent()) {
            Refund refund = (Refund) deserializer.getObject().get();
            log.info("Refund confirmed by Stripe — Refund: {} | Amount: {} {}",
                    refund.getId(), refund.getAmount(), refund.getCurrency());
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private PaymentIntent deserializePaymentIntent(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

        if (deserializer.getObject().isEmpty()) {
            log.error("Failed to deserialize PaymentIntent from event {} [{}]",
                    event.getType(), event.getId());
            return null;
        }

        return (PaymentIntent) deserializer.getObject().get();
    }
}