package com.project.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.Payment;
import com.project.backend.PaymentRepository;
import com.project.backend.entity.Company;
import com.project.backend.entity.Invoice;
import com.project.backend.enums.InvoiceStatus;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.InvoiceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CompanyRepository companyRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public List<Payment> getAllPayments(long companyId) {
        return paymentRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public Payment getPayment(long paymentId, long companyId) {
        return paymentRepository.findByIdAndCompanyIdAndActiveTrue(paymentId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }

    @Override
    @Transactional
    public Payment createPayment(long companyId, long invoiceId, Payment payment) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        payment.setCompany(company);

        Invoice invoice = invoiceRepository.findByIdAndCompanyIdAndActiveTrue(invoiceId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        if (invoice.getInvoiceStatus() == InvoiceStatus.VOID) {
            throw new IllegalStateException("Cannot pay a voided invoice");
        }

        BigDecimal remaining = invoice.getRemainingAmount().subtract(payment.getAmount());
        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Payment exceeds invoice remaining balance");
        }

        invoice.setRemainingAmount(remaining);

        if (remaining.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setInvoiceStatus(InvoiceStatus.PAID);
        } else {
            invoice.setInvoiceStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        payment.setInvoice(invoice);
        payment.setActive(true);

        invoiceRepository.save(invoice);
        return paymentRepository.save(payment);
    }


    @Override
    public Payment updatePayment(long paymentId, long companyId, Payment payment) {
        Payment existing = getPayment(paymentId, companyId);

        if (payment.getPaymentDate() != null) existing.setPaymentDate(payment.getPaymentDate());

        if (payment.getPaymentMethod() != null) existing.setPaymentMethod(payment.getPaymentMethod());

        if (payment.getReferenceNumber() != null) existing.setReferenceNumber(payment.getReferenceNumber());

        return paymentRepository.save(existing);
    }

    @Override
    public void deactivatePayment(long paymentId, long companyId) {
        Payment payment = getPayment(paymentId, companyId);
        payment.setActive(false);
        paymentRepository.save(payment);
    }
}
