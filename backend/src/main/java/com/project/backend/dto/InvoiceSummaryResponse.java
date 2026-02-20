package com.project.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Returned from GET /api/payments/invoice-summary?invoiceNumber=INV-1001
 *
 * Shown to the customer BEFORE they confirm payment so they can verify
 * the invoice details — who it's for, how much is owed, when it's due.
 *
 * No clientSecret at this stage. The PaymentIntent is only created
 * when the customer clicks "Pay Now" (POST /api/payments/initiate).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSummaryResponse {

    private String invoiceNumber;
    private String customerName;
    private BigDecimal invoiceAmount;
    private BigDecimal outstandingBalance;
    private String currency;
    private String invoiceStatus;
    private String invoiceDueDate;
}