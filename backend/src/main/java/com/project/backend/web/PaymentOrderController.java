package com.project.backend.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.service.StripeService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.backend.dto.PaymentResponse;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentOrderController {
    private final StripeService stripeService;

    @RateLimiter(name = "payment")
    @PostMapping("/create-intent")
    public ResponseEntity<PaymentResponse> createPaymentIntent(@Valid @RequestBody PaymentResponse paymentRequest){
        PaymentResponse paymentResponse = stripeService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }
}
