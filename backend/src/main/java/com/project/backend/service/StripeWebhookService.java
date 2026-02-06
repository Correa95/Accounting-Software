package com.project.backend.service;

// import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.Invoice;
import com.project.backend.entity.Payment;
import com.project.backend.enums.PaymentStatus;
import com.project.backend.repository.InvoiceRepository;
import com.project.backend.repository.PaymentOrderRepository;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripeWebhookService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final InvoiceRepository invoiceRepository;
    // private final StripeService stripeService;

    @Transactional
    public void processEvent(Event event) {

        switch (event.getType()) {

            case "payment_intent.succeeded" -> {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject()
                .orElseThrow();handleSuccess(intent);}
            case "payment_intent.payment_failed" -> {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject()
                .orElseThrow();handleFailure(intent); }
            case "payment_intent.canceled" -> {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject()
                .orElseThrow();handleCanceled(intent);}
            default -> {}
        }
    }

    private void handleSuccess(PaymentIntent intent) {
        Payment order = findPaymentOrder(intent.getId());
        if (order.getPaymentStatus() == PaymentStatus.SUCCESSFUL) return;
        order.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        applyPaymentToInvoice(order);
        paymentOrderRepository.save(order);
    }

    private void handleFailure(PaymentIntent intent) {
        Payment order = findPaymentOrder(intent.getId());
        order.setPaymentStatus(PaymentStatus.FAILED);
        paymentOrderRepository.save(order);
    }

    private void handleCanceled(PaymentIntent intent) {
        Payment order = findPaymentOrder(intent.getId());
        order.setPaymentStatus(PaymentStatus.CANCELED);
        paymentOrderRepository.save(order);
    }

    private Payment findPaymentOrder(String paymentIntentId) {
        return paymentOrderRepository.findByStripePaymentIntentId(paymentIntentId)
        .orElseThrow(() -> new RuntimeException("PaymentOrder not found for PaymentIntent " + paymentIntentId));
    }

    // private void applyPaymentToInvoice(PaymentOrder order) {
    //     Invoice invoice = order.getInvoice();
    //     BigDecimal newPaidAmount = invoice.getPaidAmount().add(order.getAmount());
    //     invoice.setPaidAmount(newPaidAmount);
    //     if (newPaidAmount.compareTo(invoice.getTotalAmount()) >= 0) {
    //         invoice.markAsPaid();
    //     }
    //     invoiceRepository.save(invoice);
    // }
    private void applyPaymentToInvoice(Payment order) {
    Invoice invoice = order.getInvoice();
    invoice.applyPayment(order.getAmount());
    invoiceRepository.save(invoice);
    }
}
