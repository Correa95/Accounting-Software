package com.project.backend.service;

import java.util.Optional;

import com.project.backend.entity.Payment;

public interface PaymentOrderService {

    Payment createPayment(Payment paymentOrder);

    void markProcessing(String stripePaymentIntentId);

    void markSuccessful(String stripePaymentIntentId);

    void markFailed(String stripePaymentIntentId, String reason);

    void markCanceled(String stripePaymentIntentId);

    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
}

