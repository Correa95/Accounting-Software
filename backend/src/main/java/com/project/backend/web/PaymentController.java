package com.project.backend.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.config.StripeConfig;
import com.project.backend.dto.CancelRefundResponse;
import com.project.backend.dto.InvoiceSummaryResponse;
import com.project.backend.dto.PartialRefundRequest;
import com.project.backend.dto.PaymentMapper;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.dto.PaymentSummaryResponse;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.service.PaymentService;
import com.project.backend.service.StripeWebhookHandler;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final StripeConfig stripeConfig;
    private final StripeWebhookHandler webhookHandler;

    // =========================================================
    // STEP 1 — Invoice lookup before checkout
    // =========================================================

    /**
     * Customer enters their invoice number.
     * Returns invoice details so they can confirm the amount
     * before we create a PaymentIntent on Stripe.
     */
    @GetMapping("/invoice-summary")
    public ResponseEntity<?> getInvoiceSummary(@RequestParam String invoiceNumber) {
        try {
            InvoiceSummaryResponse summary = paymentService.getInvoiceSummary(invoiceNumber);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            log.warn("Invoice lookup failed for {}: {}", invoiceNumber, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================
    // STEP 2 — Initiate payment, get clientSecret for Stripe.js
    // =========================================================

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestParam String invoiceNumber) {
        try {
            return ResponseEntity.ok(paymentService.initiatePayment(invoiceNumber));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        } catch (Exception e) {
            log.error("Failed to initiate payment for invoice {}: {}", invoiceNumber, e.getMessage());
            return ResponseEntity.internalServerError().body(error("Payment initiation failed."));
        }
    }

    // =========================================================
    // CANCEL
    // =========================================================

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestParam String invoiceNumber) {
        try {
            CancelRefundResponse response = paymentService.cancelPayment(invoiceNumber);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        } catch (Exception e) {
            log.error("Cancel failed for invoice {}: {}", invoiceNumber, e.getMessage());
            return ResponseEntity.internalServerError().body(error("Cancellation failed."));
        }
    }

    // =========================================================
    // REFUNDS
    // =========================================================

    @PostMapping("/refund/full")
    public ResponseEntity<?> fullRefund(@RequestParam String invoiceNumber) {
        try {
            CancelRefundResponse response = paymentService.refundPayment(invoiceNumber);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        } catch (Exception e) {
            log.error("Full refund failed for invoice {}: {}", invoiceNumber, e.getMessage());
            return ResponseEntity.internalServerError().body(error("Refund failed."));
        }
    }

    @PostMapping("/refund/partial")
    public ResponseEntity<?> partialRefund(@Valid @RequestBody PartialRefundRequest request) {
        try {
            CancelRefundResponse response = paymentService.partialRefundPayment(
                    request.getInvoiceNumber(), request.getRefundAmount());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        } catch (Exception e) {
            log.error("Partial refund failed for {}: {}", request.getInvoiceNumber(), e.getMessage());
            return ResponseEntity.internalServerError().body(error("Partial refund failed."));
        }
    }

    // =========================================================
    // STRIPE WEBHOOK
    // =========================================================

    /**
     * Receives raw Stripe events and verifies their signature.
     *
     * IMPORTANT: The @RequestBody must be String — NOT a parsed object.
     * Stripe computes the signature over the raw bytes. If Spring parses
     * the body first the signature check will always fail.
     *
     * Signature verification is handled here before delegating to
     * StripeWebhookHandler — keeping security concerns in the controller.
     */
    @PostMapping("/webhook")
    public ResponseEntity<Map<String, String>> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeConfig.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            log.warn("Stripe webhook signature verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(error("Invalid signature."));
        } catch (Exception e) {
            log.error("Stripe webhook processing error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(error("Webhook error."));
        }

        // Delegate event routing to the handler — controller stays thin
        webhookHandler.handle(event);

        return ResponseEntity.ok(Map.of("status", "received", "eventType", event.getType()));
    }

    // =========================================================
    // QUERY ENDPOINTS
    // =========================================================

    @GetMapping("/by-invoice/{invoiceId}")
    public ResponseEntity<List<PaymentSummaryResponse>> getByInvoice(
            @PathVariable Long invoiceId) {
        return ResponseEntity.ok(
                PaymentMapper.toSummaryList(paymentService.getPaymentsByInvoice(invoiceId)));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<PaymentSummaryResponse>> getByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(
                PaymentMapper.toSummaryList(paymentService.getPaymentsByCustomer(customerId)));
    }

    @GetMapping("/by-customer/{customerId}/status")
    public ResponseEntity<List<PaymentSummaryResponse>> getByCustomerAndStatus(
            @PathVariable Long customerId,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(
                PaymentMapper.toSummaryList(
                        paymentService.getPaymentsByCustomerAndStatus(customerId, status)));
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<PaymentSummaryResponse>> getByStatus(
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(
                PaymentMapper.toSummaryList(paymentService.getPaymentsByStatus(status)));
    }

    @GetMapping("/by-currency")
    public ResponseEntity<List<PaymentSummaryResponse>> getByCurrency(
            @RequestParam String currency) {
        return ResponseEntity.ok(
                PaymentMapper.toSummaryList(paymentService.getPaymentsByCurrency(currency)));
    }

    @GetMapping("/by-currency/status")
    public ResponseEntity<List<PaymentSummaryResponse>> getByCurrencyAndStatus(
            @RequestParam String currency,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(
                PaymentMapper.toSummaryList(
                        paymentService.getPaymentsByCurrencyAndStatus(currency, status)));
    }

    @GetMapping("/intent/{intentId}")
    public ResponseEntity<PaymentResponse> getByIntentId(@PathVariable String intentId) {
        return ResponseEntity.ok(
                PaymentMapper.toResponse(paymentService.getPaymentByIntentId(intentId)));
    }

    // =========================================================
    // HELPER
    // =========================================================

    private Map<String, String> error(String message) {
        return Map.of("error", message);
    }
}