package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Payment;

public interface PaymentService {
    List<Payment> getAllPayments(long companyId);
    Payment getPayment(long paymentId, long companyId);
    Payment createPayment(long companyId, long invoiceId, Payment payment);
    Payment updatePayment(long paymentId, long companyId, Payment payment);
    void deactivatePayment(long paymentId, long companyId);
}
