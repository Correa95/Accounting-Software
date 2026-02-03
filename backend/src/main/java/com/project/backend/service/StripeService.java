package com.project.backend.service;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;

public interface StripeService {

    PaymentResponse createPayment(PaymentRequest paymentRequest);

    void handleWebhook(String payload, String sigHeader);
}
