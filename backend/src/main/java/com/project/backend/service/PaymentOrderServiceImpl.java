package com.project.backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.PaymentOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final JournalEntryService journalEntryService;

    @Override
    @Transactional
    public PaymentOrder createPayment(PaymentOrder paymentOrder) {
        paymentOrder.setPaymentStatus(PaymentStatus.PENDING);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    @Transactional
    public PaymentOrder markProcessing(String stripePaymentIntentId) {
        PaymentOrder order = getPaymentOrder(stripePaymentIntentId);
        order.setPaymentStatus(PaymentStatus.PROCESSING);
        return paymentOrderRepository.save(order);
    }

    @Override
    @Transactional
    public PaymentOrder markSuccessful(String stripePaymentIntentId) {
        PaymentOrder order = getPaymentOrder(stripePaymentIntentId);
        order.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        PaymentOrder savedOrder = paymentOrderRepository.save(order);

        // Record journal entry for successful payment
        journalEntryService.recordStripePayment(savedOrder);

        return savedOrder;
    }

    @Override
    @Transactional
    public PaymentOrder markFailed(String stripePaymentIntentId, String reason) {
        PaymentOrder order = getPaymentOrder(stripePaymentIntentId);
        order.setPaymentStatus(PaymentStatus.FAILED);
        order.setFailureReason(reason);
        return paymentOrderRepository.save(order);
    }

    @Override
    @Transactional
    public PaymentOrder markCanceled(String stripePaymentIntentId) {
        PaymentOrder order = getPaymentOrder(stripePaymentIntentId);
        order.setPaymentStatus(PaymentStatus.CANCELED);
        return paymentOrderRepository.save(order);
    }

    @Override
    public Optional<PaymentOrder> findByStripePaymentIntentId(String stripePaymentIntentId) {
        return paymentOrderRepository.findByStripePaymentIntentId(stripePaymentIntentId);
    }

    // ----------------------------
    // INTERNAL HELPER
    // ----------------------------
    private PaymentOrder getPaymentOrder(String stripePaymentIntentId) {
        return paymentOrderRepository.findByStripePaymentIntentId(stripePaymentIntentId)
                .orElseThrow(() -> new RuntimeException("PaymentOrder not found for ID: " + stripePaymentIntentId));
    }
}
