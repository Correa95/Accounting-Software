package com.project.backend.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;

import lombok.extern.slf4j.Slf4j;

/**
 * Translates Stripe SDK exceptions into clean HTTP responses.
 *
 * Without this, a StripeException thrown from PaymentService would
 * propagate as a 500 Internal Server Error with a raw stack trace.
 * This handler maps each Stripe exception type to the appropriate
 * HTTP status and a safe user-facing message.
 *
 * Stripe exception hierarchy:
 *   StripeException
 *     ├── CardException          — card was declined (show to user)
 *     ├── AuthenticationException — bad API key (config problem)
 *     ├── InvalidRequestException — bad parameters (our bug)
 *     ├── RateLimitException      — too many requests
 *     └── ApiException            — Stripe server error
 */
@Slf4j
@RestControllerAdvice
public class StripeExceptionHandler {

    /**
     * Card declined — safe to show the Stripe message to the user.
     * e.g. "Your card has insufficient funds."
     * e.g. "Your card was declined."
     */
    @ExceptionHandler(CardException.class)
    public ResponseEntity<Map<String, String>> handleCardException(CardException e) {
        log.warn("Card declined — code: {} | message: {}", e.getCode(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.PAYMENT_REQUIRED)
                .body(Map.of(
                        "error", e.getMessage(),
                        "code", e.getCode() != null ? e.getCode() : "card_declined"
                ));
    }

    /**
     * Bad API key — configuration problem, never expose details to the user.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthException(AuthenticationException e) {
        log.error("Stripe authentication failed — check your API keys: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Payment service configuration error. Contact support."));
    }

    /**
     * Invalid request — wrong parameters sent to Stripe.
     * Usually our bug (e.g. negative amount, invalid currency code).
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRequest(InvalidRequestException e) {
        log.error("Invalid Stripe request — param: {} | message: {}", e.getParam(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid payment request: " + e.getMessage()));
    }

    /**
     * Rate limit — too many API calls too quickly.
     */
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Map<String, String>> handleRateLimit(RateLimitException e) {
        log.warn("Stripe rate limit hit: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("error", "Too many payment requests. Please try again shortly."));
    }

    /**
     * Catch-all for any other Stripe exception.
     * e.g. Stripe API outage, network issues.
     */
    @ExceptionHandler(StripeException.class)
    public ResponseEntity<Map<String, String>> handleStripeException(StripeException e) {
        log.error("Stripe error [{}] — requestId: {} | message: {}",
                e.getStatusCode(), e.getRequestId(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(Map.of(
                        "error", "Payment service temporarily unavailable. Please try again.",
                        "requestId", e.getRequestId() != null ? e.getRequestId() : "unknown"
                ));
    }

    /**
     * Our own domain validation errors — IllegalStateException and IllegalArgumentException
     * thrown by the service layer (e.g. "Cannot cancel a paid invoice").
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException e) {
        log.warn("Business rule violation: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("Invalid argument: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

    /**
     * Entity not found — invoice or payment lookup failed.
     */
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            jakarta.persistence.EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }
}