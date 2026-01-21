package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Invoice;

public interface InvoiceService {
    List<Invoice> getAllInvoices(Long companyId);
    Invoice getInvoiceById(Long invoiceId, Long companyId);
    Invoice createInvoice(Invoice invoice, Long companyId, long customerId);
    Invoice updateInvoice(Long invoiceId, Long companyId, Invoice invoice);
    void deactivateInvoice(Long invoiceId, Long companyId);
}
