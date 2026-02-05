package com.project.backend.service;

import java.util.Optional;

import com.project.backend.entity.PaymentOrder;

public interface PaymentOrderService {

    Optional<PaymentOrder> findByStripePaymentIntentId(String paymentIntentId);
    PaymentOrder createPayment(PaymentOrder paymentOrder);

    void markSuccessful(String paymentIntentId);

    void markFailed(String paymentIntentId, String reason);

    void markCanceled(String paymentIntentId);

    void markProcessing(String paymentIntentId);
}
