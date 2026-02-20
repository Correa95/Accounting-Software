package com.project.backend.extra;

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
public class PaymentOrderResponse {

    private Long invoiceId;
    private String paymentIntentId;
    private String clientSecret;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String message;

    
}
