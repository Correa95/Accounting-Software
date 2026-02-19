package com.project.backend.extra;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.dto.PaymentRequest;
import com.project.backend.entity.Invoice;
import com.project.backend.entity.PaymentOrder;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.InvoiceRepository;
import com.project.backend.service.JournalEntryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;
    // private final InvoiceRepository invoiceRepository;
    // private final JournalEntryService journalEntryService;

    // ----------------------------
    // MAKE
    // ----------------------------
    @Override
    @Transactional
    public PaymentOrder markProcess(long invoiceId, long customerId, PaymentRequest paymentRequest) {
        PaymentRequest paymentRequest.findByPayment(invoiceId, customerId, paymentRequest);
        paymentOrder.setPaymentStatus(paymenrRequest, PaymentStatus.PENDING);
        return paymentOrderRepository.save(paymentRequest,paymentOrder);
    }

    // ----------------------------
    // STATUS TRANSITIONS
    // ----------------------------
    // @Override
    // @Transactional
    // public void markProcessing(String stripePaymentIntentId) {
    //     Payment order = getOrder(stripePaymentIntentId);
    //     order.setPaymentStatus(PaymentStatus.PROCESSING);
    //     paymentOrderRepository.save(order);
    // }

    // @Override
    // @Transactional
    // public void markSuccessful(String stripePaymentIntentId) {
    //     Payment order = getOrder(stripePaymentIntentId);

    //     if (order.getPaymentStatus() == PaymentStatus.SUCCESSFUL) {
    //         return; 
    //     }

    //     order.setPaymentStatus(PaymentStatus.SUCCESSFUL);

    //     // Update invoice balance
    //     Invoice invoice = order.getInvoice();
    //     invoice.setOutstandingBalance(invoice.getOutstandingBalance().subtract(order.getAmount()));

    //     paymentOrderRepository.save(order);
    //     invoiceRepository.save(invoice);

    //     // Record accounting entry
    //     journalEntryService.recordStripePayment(order);
    // }

    // @Override
    // @Transactional
    // public void markFailed(String stripePaymentIntentId, String reason) {
    //     Payment order = getOrder(stripePaymentIntentId);
    //     order.setPaymentStatus(PaymentStatus.FAILED);
    //     order.setFailureReason(reason);
    //     paymentOrderRepository.save(order);
    // }

    // @Override
    // @Transactional
    // public void markCanceled(String stripePaymentIntentId) {
    //     Payment order = getOrder(stripePaymentIntentId);
    //     order.setPaymentStatus(PaymentStatus.CANCELED);
    //     paymentOrderRepository.save(order);
    // }

    // ----------------------------
    // QUERIES
    // ----------------------------
    // @Override
    // public Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId) {
    //     return paymentOrderRepository.findByStripePaymentIntentId(stripePaymentIntentId);
    // }

    // ----------------------------
    // INTERNAL HELPER
    // ----------------------------
    // private Payment getOrder(String stripePaymentIntentId) {
    //     return paymentOrderRepository.findByStripePaymentIntentId(stripePaymentIntentId).orElseThrow(() ->
    //         new RuntimeException("PaymentOrder not found for Stripe ID: " + stripePaymentIntentId));
    // }
}
