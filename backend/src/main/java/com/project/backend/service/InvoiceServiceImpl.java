package com.project.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.Company;
import com.project.backend.entity.Customer;
import com.project.backend.entity.Invoice;
import com.project.backend.entity.Payment;
import com.project.backend.enums.InvoiceStatus;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.CustomerRepository;
import com.project.backend.repository.InvoiceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getAllInvoices(long companyId) {
        return invoiceRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice getInvoiceById(long invoiceId, long companyId) {
        return invoiceRepository.findByIdAndCompanyIdAndActiveTrue(invoiceId, companyId)
                .orElseThrow(() -> new EntityNotFoundException( "Invoice not found with id: " + invoiceId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByStatus(long companyId, InvoiceStatus status) {
        return invoiceRepository.findByCompanyIdAndInvoiceStatusAndActiveTrue(companyId, status);
    }

   

    @Override
    public Invoice createInvoice(long companyId, long customerId, Invoice invoice) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new                   EntityNotFoundException("Company not found with id: " + companyId));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Customer not found with id: " + customerId));

        Long nextSeq = jdbcTemplate.queryForObject(
                "SELECT nextval('invoice_number_seq')", Long.class);

        String year = String.valueOf(LocalDate.now().getYear());
        String invoiceNumber = String.format("INV-%s-%06d", year, nextSeq);

        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setInvoiceStatus(InvoiceStatus.DRAFT);
        invoice.setCompany(company);
        invoice.setCustomer(customer);
        invoice.setActive(true);

        log.info("Created invoice {} for company {} customer {}",
                invoiceNumber, companyId, customerId);

        return invoiceRepository.save(invoice);
    }

    

    @Override
    public Invoice updateInvoice(long invoiceId, long companyId, Invoice invoice) {
        Invoice existing = getInvoiceById(invoiceId, companyId);

        if (existing.getInvoiceStatus() != InvoiceStatus.DRAFT) {
            throw new IllegalStateException(
                "Only DRAFT invoices can be edited. Current status: "+ existing.getInvoiceStatus());
        }

        if (invoice.getInvoiceDate() != null)
            existing.setInvoiceDate(invoice.getInvoiceDate());

        if (invoice.getInvoiceDueDate() != null)
            existing.setInvoiceDueDate(invoice.getInvoiceDueDate());

        if (invoice.getInvoiceAmount() != null) {
            existing.setInvoiceAmount(invoice.getInvoiceAmount());
            // Keep outstandingBalance in sync when amount changes on a DRAFT
            existing.setOutstandingBalance(invoice.getInvoiceAmount());
        }

        if (invoice.getCurrency() != null)
            existing.setCurrency(invoice.getCurrency());

        if (invoice.getAccount() != null)
            existing.setAccount(invoice.getAccount());

        return invoiceRepository.save(existing);
    }

    @Override
    public Invoice sendInvoice(long invoiceId, long companyId) {
        Invoice invoice = getInvoiceById(invoiceId, companyId);

        if (invoice.getInvoiceStatus() != InvoiceStatus.DRAFT) {
            throw new IllegalStateException(
                "Only DRAFT invoices can be sent. Current status: "
                + invoice.getInvoiceStatus());
        }

        if (invoice.getInvoiceAmount() == null
                || invoice.getInvoiceAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException(
                "Invoice amount must be set and greater than zero before sending.");
        }

        if (invoice.getInvoiceDueDate() == null) {
            throw new IllegalStateException(
                "Invoice due date must be set before sending.");
        }

        invoice.setInvoiceStatus(InvoiceStatus.SENT);

        log.info("Invoice {} sent for company {}", invoice.getInvoiceNumber(), companyId);

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice voidInvoice(long invoiceId, long companyId) {
        Invoice invoice = getInvoiceById(invoiceId, companyId);

        if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException(
                "Paid invoices cannot be voided. Issue a refund instead.");
        }

        if (invoice.getInvoiceStatus() == InvoiceStatus.VOID) {
            throw new IllegalStateException(
                "Invoice is already voided.");
        }

        if (invoice.getInvoiceStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException(
                "Invoice is already cancelled.");
        }

        invoice.cancel();
        invoice.setInvoiceStatus(InvoiceStatus.VOID);

        log.info("Invoice {} voided for company {}", invoice.getInvoiceNumber(), companyId);

        return invoiceRepository.save(invoice);
    }
    
    @Override
    public void deactivateInvoice(long invoiceId, long companyId) {
        Invoice invoice = getInvoiceById(invoiceId, companyId);

        if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException( "Paid invoices cannot be deactivated — they are part of the financial record.");
        }

        invoice.setActive(false);
        invoiceRepository.save(invoice);

        log.info("Invoice {} deactivated for company {}", invoice.getInvoiceNumber(), companyId);
    }

    
    @Override
    public Invoice markInvoicePaid(Payment payment) {
        Invoice invoice = invoiceRepository.findByPayment(payment)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Invoice not found for payment id: " + payment.getId()));

        if (invoice.getInvoiceStatus() == InvoiceStatus.VOID) {
            throw new IllegalStateException("Voided invoices cannot be marked as paid.");
        }

        if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
            log.warn("Invoice {} is already paid — skipping duplicate webhook",
                    invoice.getInvoiceNumber());
            return invoice;
        }

        invoice.applyPayment(payment.getAmount());

        log.info("Invoice {} marked PAID via payment {}",
                invoice.getInvoiceNumber(), payment.getStripePaymentIntentId());

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice applyRefund(long invoiceId, BigDecimal refundAmount) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Invoice not found with id: " + invoiceId));

        if (invoice.getInvoiceStatus() != InvoiceStatus.PAID
                && invoice.getInvoiceStatus() != InvoiceStatus.PARTIAL_REFUND) {
            throw new IllegalStateException(
                "Only PAID or PARTIALLY_REFUNDED invoices can have refunds applied.");
        }

        invoice.applyRefund(refundAmount);

        log.info("Refund of {} applied to invoice {}", refundAmount, invoice.getInvoiceNumber());

        return invoiceRepository.save(invoice);
    }
}