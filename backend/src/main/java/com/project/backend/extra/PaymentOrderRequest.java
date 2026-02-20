package com.project.backend.extra;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.50", message = "Minimum amount is 0.50")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private Long customerId;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private Long customerId;
    // @NotBlank(message = "Customer email is required")
    // @Email(message = "Invalid email format")
    // private String customerEmail;

    @NotBlank(message = "Description is required")
    private String description;
}
    

