package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Payment;

public interface PaymentService {
    List<Payment> getAllPayments(Long companyId);
    Payment getPayment(Long paymentId, Long companyId);
    Payment createPayment(Payment payment, Long companyId);
    Payment updatePayment(Long paymentId, Long companyId, Payment payment);
    void deactivatePayment(Long paymentId, Long companyId);
}
