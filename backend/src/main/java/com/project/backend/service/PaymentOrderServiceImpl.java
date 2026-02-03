package com.project.backend.service;

import com.project.backend.entity.Invoice;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.PaymentOrderRepository;
// import com.project.backend.service.AccountService;
// import com.project.backend.service.InvoiceService;
// import com.project.backend.service.PaymentOrderService;
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
    public void handlePaymentSucceeded(PaymentIntent paymentIntent) {
        PaymentOrder paymentOrder = paymentOrderRepository.findByStripePaymentIntentId(paymentIntent.getId()).orElseThrow(() ->new IllegalStateException("PaymentOrder not found"));
        if (paymentOrder.getPaymentStatus() == PaymentStatus.SUCCESSFUL) {
            return; // idempotency guard
        }

        paymentOrder.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        paymentOrderRepository.save(paymentOrder);

        Invoice invoice = invoiceService.markInvoicePaid(paymentOrder);

        accountService.postPayment(invoice, paymentOrder);
    }

    @Override
    public void handlePaymentFailed(PaymentIntent paymentIntent) {

        PaymentOrder paymentOrder = paymentOrderRepository.findByStripePaymentIntentId(paymentIntent.getId()).orElseThrow(() ->new IllegalStateException("PaymentOrder not found"));

        paymentOrder.setPaymentStatus(PaymentStatus.FAILED);
        paymentOrderRepository.save(paymentOrder);
    }
}
