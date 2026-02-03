package com.project.backend.service;

import com.stripe.model.PaymentIntent;

public interface PaymentOrderService {

    void handlePaymentSucceeded(PaymentIntent paymentIntent);

    void handlePaymentFailed(PaymentIntent paymentIntent);
}

