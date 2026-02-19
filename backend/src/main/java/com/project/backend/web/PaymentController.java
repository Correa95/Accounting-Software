package com.project.backend.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.service.PaymentService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentIntentResponse> createPayment(
            @RequestBody @Validated PaymentRequest request) {

        // Delegate everything to StripeService
        PaymentResponse response = stripeService.createPaymentIntent(request);

        // Return response with 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
