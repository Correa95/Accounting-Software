package com.project.backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.Invoice;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.InvoiceRepository;
import com.project.backend.repository.PaymentOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final JournalEntryService journalEntryService;

    // ----------------------------
    // CREATE
    // ----------------------------
    @Override
    @Transactional
    public PaymentOrder createPayment(PaymentOrder paymentOrder) {
        paymentOrder.setPaymentStatus(PaymentStatus.PENDING);
        return paymentOrderRepository.save(paymentOrder);
    }

    // ----------------------------
    // STATUS TRANSITIONS
    // ----------------------------
    @Override
    @Transactional
    public void markProcessing(String stripePaymentIntentId) {
        PaymentOrder order = getOrder(stripePaymentIntentId);
        order.setPaymentStatus(PaymentStatus.PROCESSING);
        paymentOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void markSuccessful(String stripePaymentIntentId) {
        PaymentOrder order = getOrder(stripePaymentIntentId);

        if (order.getPaymentStatus() == PaymentStatus.SUCCESSFUL) {
            return; 
        }

        order.setPaymentStatus(PaymentStatus.SUCCESSFUL);

        // Update invoice balance
        Invoice invoice = order.getInvoice();
        invoice.setOutstandingBalance(invoice.getOutstandingBalance().subtract(order.getAmount()));

        paymentOrderRepository.save(order);
        invoiceRepository.save(invoice);

        // Record accounting entry
        journalEntryService.recordStripePayment(order);
    }

    @Override
    @Transactional
    public void markFailed(String stripePaymentIntentId, String reason) {
        PaymentOrder order = getOrder(stripePaymentIntentId);
        order.setPaymentStatus(PaymentStatus.FAILED);
        order.setFailureReason(reason);
        paymentOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void markCanceled(String stripePaymentIntentId) {
        PaymentOrder order = getOrder(stripePaymentIntentId);
        order.setPaymentStatus(PaymentStatus.CANCELED);
        paymentOrderRepository.save(order);
    }

    // ----------------------------
    // QUERIES
    // ----------------------------
    @Override
    public Optional<PaymentOrder> findByStripePaymentIntentId(String stripePaymentIntentId) {
        return paymentOrderRepository.findByStripePaymentIntentId(stripePaymentIntentId);
    }

    // ----------------------------
    // INTERNAL HELPER
    // ----------------------------
    private PaymentOrder getOrder(String stripePaymentIntentId) {
        return paymentOrderRepository.findByStripePaymentIntentId(stripePaymentIntentId).orElseThrow(() ->
            new RuntimeException("PaymentOrder not found for Stripe ID: " + stripePaymentIntentId));
    }
}
