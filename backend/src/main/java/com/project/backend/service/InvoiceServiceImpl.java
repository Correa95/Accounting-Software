package com.project.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.Company;
import com.project.backend.entity.Invoice;
import com.project.backend.enums.InvoiceStatus;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.InvoiceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Invoice> getAllInvoices(long companyId) {
        return invoiceRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public Invoice getInvoiceById(long invoiceId, long companyId) {
        return invoiceRepository.findByIdAndCompanyIdAndActiveTrue(invoiceId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
    }

    @Override
    @Transactional
    public Invoice createInvoice(long companyId, long customerId, Invoice invoice) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        long nextSeq = jdbcTemplate.queryForObject(
                "SELECT nextval('invoice_number_seq')",
                long.class
        );

        String year = String.valueOf(LocalDate.now().getYear());
        String invoiceNumber = String.format("INV-%s-%06d", year, nextSeq);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setInvoiceStatus(InvoiceStatus.DRAFT);
        invoice.setCompany(company);
        invoice.setActive(true);
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice updateInvoice(long invoiceId, long companyId, Invoice invoice) {

    Invoice existingInvoice = getInvoiceById(invoiceId, companyId);

    if (existingInvoice.getInvoiceStatus() != InvoiceStatus.DRAFT) {
        throw new IllegalStateException(
            "Only DRAFT invoices can be edited"
        );
    }

    if (invoice.getInvoiceDate() != null)
        existingInvoice.setInvoiceDate(invoice.getInvoiceDate());

    if (invoice.getInvoiceDueDate() != null)
        existingInvoice.setInvoiceDueDate(invoice.getInvoiceDueDate());

    if (invoice.getInvoiceAmount() != null)
        existingInvoice.setInvoiceAmount(invoice.getInvoiceAmount());

    return invoiceRepository.save(existingInvoice);
    }

    @Override
    @Transactional
    public Invoice sendInvoice(long invoiceId, long companyId) {
    Invoice invoice = getInvoiceById(invoiceId, companyId);
    if (invoice.getInvoiceStatus() != InvoiceStatus.DRAFT) {
        throw new IllegalStateException("Only DRAFT invoices can be sent");
    }
    invoice.setInvoiceStatus(InvoiceStatus.SENT);
    return invoiceRepository.save(invoice);

    }
    @Override
    @Transactional
    public Invoice voidInvoice(long invoiceId, long companyId) {
    Invoice invoice = getInvoiceById(invoiceId, companyId);
    if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
        throw new IllegalStateException("Paid invoices cannot be voided");
    }
    invoice.setInvoiceStatus(InvoiceStatus.VOID);
    return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice markInvoicePaid(long invoiceId, long companyId) {
    Invoice invoice = getInvoiceById(invoiceId, companyId);
    if (invoice.getInvoiceStatus() == InvoiceStatus.VOID) {
        throw new IllegalStateException("Voided invoices cannot be paid");
    }
    invoice.setInvoiceStatus(InvoiceStatus.PAID);
    return invoiceRepository.save(invoice);
}




    
    @Override
    @Transactional
    public void deactivateInvoice(long invoiceId, long companyId) {
        Invoice invoice = getInvoiceById(invoiceId, companyId);
        invoice.setActive(false);
        invoiceRepository.save(invoice);
    }
}
