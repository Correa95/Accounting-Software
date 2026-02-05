package com.project.backend.dto;

import java.math.BigDecimal;

import com.project.backend.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long invoiceId;

    private String paymentIntentId;

    /** Stripe client secret (frontend confirmation) */
    private String clientSecret;

    private BigDecimal amount;

    private String currency;

    private PaymentStatus paymentStatus;

    private String message;
}
