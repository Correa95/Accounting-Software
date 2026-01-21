package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Company;
import com.project.backend.entity.Invoice;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.InvoiceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<Invoice> getAllInvoices(Long companyId) {
        return invoiceRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public Invoice getInvoiceById(Long invoiceId, Long companyId) {
        return invoiceRepository.findByIdAndCompanyIdAndActiveTrue(invoiceId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
    }

    @Override
    public Invoice createInvoice(Invoice invoice, Long companyId, long customerId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        invoice.setCompany(company);
        invoice.setActive(true);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateInvoice(Long invoiceId, Long companyId, Invoice invoice) {
        Invoice existing = getInvoiceById(invoiceId, companyId);
        if (invoice.getCustomer() != null) existing.setCustomer(invoice.getCustomer());
        if (invoice.getInvoiceAmount() != null) existing.setInvoiceAmount(invoice.getInvoiceAmount());
        if (invoice.getInvoiceDate() != null) existing.setInvoiceDate(invoice.getInvoiceDate());
        if (invoice.getInvoiceDueDate() != null) existing.setInvoiceDueDate(invoice.getInvoiceDueDate());
        return invoiceRepository.save(existing);
    }

    @Override
    public void deactivateInvoice(Long invoiceId, Long companyId) {
        Invoice invoice = getInvoiceById(invoiceId, companyId);
        invoice.setActive(false);
        invoiceRepository.save(invoice);
    }
}
