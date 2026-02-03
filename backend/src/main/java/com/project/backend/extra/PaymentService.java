package com.project.backend.extra;

import java.util.List;

public interface PaymentService {
    List<Payment> getAllPayments(long companyId);
    Payment getPayment(long paymentId, long companyId);
    Payment createPayment(long companyId, long invoiceId, Payment payment);
    Payment updatePayment(long paymentId, long companyId, Payment payment);
    void deactivatePayment(long paymentId, long companyId);
}
