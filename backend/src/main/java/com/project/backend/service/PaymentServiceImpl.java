package com.project.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.project.backend.entity.Payment;
import com.project.backend.entity.Company;
import com.project.backend.repository.PaymentRepository;
import com.project.backend.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<Payment> getAllPayments(Long companyId) {
        return paymentRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public Payment getPayment(Long paymentId, Long companyId) {
        return paymentRepository.findByIdAndCompanyIdAndActiveTrue(paymentId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }

    @Override
    public Payment createPayment(Payment payment, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        payment.setCompany(company);
        payment.setActive(true);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Long paymentId, Long companyId, Payment payment) {
        Payment existing = getPayment(paymentId, companyId);
        if (payment.getAmount() != null) existing.setAmount(payment.getAmount());
        if (payment.getPaymentDate() != null) existing.setPaymentDate(payment.getPaymentDate());
        if (payment.getPaymentMethod() != null) existing.setPaymentMethod(payment.getPaymentMethod());
        if (payment.getReferenceNumber() != null) existing.setReferenceNumber(payment.getReferenceNumber());
        return paymentRepository.save(existing);
    }

    @Override
    public void deactivatePayment(Long paymentId, Long companyId) {
        Payment payment = getPayment(paymentId, companyId);
        payment.setActive(false);
        paymentRepository.save(payment);
    }
}
