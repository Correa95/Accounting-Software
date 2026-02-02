package com.project.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.PaymentOrderRepository;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional

public class PaymentOrderService {
    private final PaymentOrderRepository paymentOrderRepository;
    private final InvoiceService invoiceService;
    private final AccountService accountService;

    public void handlePaymentSucceeded(PaymentIntent intent) {

        PaymentOrder order = paymentOrderRepository
                .findByStripePaymentIntentId(intent.getId())
                .orElseThrow();

        order.setPaymentStatus(PaymentStatus.SUCCEEDED);
        paymentOrderRepository.save(order);

        // 1️⃣ Mark invoice as paid
        Invoice invoice = invoiceService.markInvoicePaid(order);

        // 2️⃣ Post accounting entries
        accountService.postPayment(invoice, order);
    }

    public void handlePaymentFailed(PaymentIntent intent) {
        PaymentOrder order = paymentOrderRepository
                .findByStripePaymentIntentId(intent.getId())
                .orElseThrow();

        order.setPaymentStatus(PaymentStatus.FAILED);
        paymentOrderRepository.save(order);
    }
}
