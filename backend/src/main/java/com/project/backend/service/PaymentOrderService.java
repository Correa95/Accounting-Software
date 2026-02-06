package com.project.backend.service;

import java.util.Optional;

import com.project.backend.entity.PaymentOrder;

public interface PaymentOrderService {

    PaymentOrder createPayment(PaymentOrder paymentOrder);

    void markProcessing(String stripePaymentIntentId);

    void markSuccessful(String stripePaymentIntentId);

    void markFailed(String stripePaymentIntentId, String reason);

    void markCanceled(String stripePaymentIntentId);

    Optional<PaymentOrder> findByStripePaymentIntentId(String stripePaymentIntentId);
}

