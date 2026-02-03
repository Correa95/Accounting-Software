package com.project.backend.service;

import com.project.backend.entity.Invoice;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.PaymentOrderRepository;
import com.project.backend.service.AccountService;
import com.project.backend.service.InvoiceService;
import com.project.backend.service.PaymentOrderService;
import com.stripe.model.PaymentIntent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final InvoiceService invoiceService;
    private final AccountService accountService;

    @Override
    public void handlePaymentSucceeded(PaymentIntent intent) {

        PaymentOrder order = paymentOrderRepository
                .findByStripePaymentIntentId(intent.getId())
                .orElseThrow(() ->
                        new IllegalStateException("PaymentOrder not found"));

        if (order.getPaymentStatus() == PaymentStatus.SUCCESSFUL) {
            return; // idempotency guard
        }

        order.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        paymentOrderRepository.save(order);

        Invoice invoice = invoiceService.markInvoicePaid(order);

        accountService.postPayment(invoice, order);
    }

    @Override
    public void handlePaymentFailed(PaymentIntent intent) {

        PaymentOrder order = paymentOrderRepository
                .findByStripePaymentIntentId(intent.getId())
                .orElseThrow(() ->
                        new IllegalStateException("PaymentOrder not found"));

        order.setPaymentStatus(PaymentStatus.FAILED);
        paymentOrderRepository.save(order);
    }
}
