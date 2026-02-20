package com.project.backend.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.service.PaymentService;
import com.stripe.model.PaymentIntent;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody @Validated PaymentRequest request) {
                try {
                     PaymentIntent intent = paymentService.createPayment(request);

                    PaymentResponse response = new PaymentResponse(
                        intent.getId(),
                        intent.getClientSecret(),
                        intent.getAmount(),
                        intent.getCurrency(),
                        intent.getStatus());
                        return ResponseEntity.ok(response);
                } catch (Exception e) {
                    return ResponseEntity.status(500).build();
                }

            }
        }
        