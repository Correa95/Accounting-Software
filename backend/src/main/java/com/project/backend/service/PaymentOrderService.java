package com.project.backend.service;

import com.project.backend.dto.PaymentRequest;


public interface  PaymentOrderService {
    void markProcess(PaymentRequest paymentRequest);

    // void markSuccessful(String stripePaymentIntentId);

    // void markFailed(String stripePaymentIntentId, String reason);

    // void markCanceled(String stripePaymentIntentId);

}
