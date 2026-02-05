package com.project.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.dto.PaymentResponse;
import com.project.backend.entity.Customer;
import com.project.backend.entity.Invoice;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.CustomerRepository;
import com.project.backend.repository.InvoiceRepository;
import com.project.backend.service.PaymentOrderService;
import com.project.backend.service.StripeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentOrderController {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentOrderService paymentOrderService;
    private final StripeService stripeService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPaymentIntent(
            @RequestBody @Validated PaymentRequest request) {

        // 1️⃣ Load customer & invoice
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // 2️⃣ Call StripeService to create PaymentIntent
        PaymentResponse stripeResponse = stripeService.createPaymentIntent(request, customer, invoice);

        return new ResponseEntity<>(stripeResponse, HttpStatus.CREATED);
    }
}
