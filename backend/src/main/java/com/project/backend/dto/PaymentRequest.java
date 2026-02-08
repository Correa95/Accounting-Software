package com.project.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be valid")
    private Long customerId;

    @NotNull(message = "Invoice ID is required")
    @Positive(message = "Invoice ID must be valid")
    private Long invoiceId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.50", message = "Minimum amount is 0.50")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;

    @NotBlank(message = "Message is required")
    private String message;
}
