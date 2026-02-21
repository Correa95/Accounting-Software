package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Invoice;
import com.project.backend.entity.Payment;
import com.project.backend.enums.InvoiceStatus;

public interface InvoiceService {

    List<Invoice> getAllInvoices(long companyId);
    Invoice getInvoiceById(long invoiceId, long companyId);
    List<Invoice> getInvoicesByStatus(long companyId, InvoiceStatus status);

    Invoice createInvoice(long companyId, long customerId, Invoice invoice);
    Invoice updateInvoice(long invoiceId, long companyId, Invoice invoice);
    Invoice sendInvoice(long invoiceId, long companyId);
    Invoice voidInvoice(long invoiceId, long companyId);
    void deactivateInvoice(long invoiceId, long companyId);

     
    Invoice markInvoicePaid(Payment payment);
    Invoice applyRefund(long invoiceId, java.math.BigDecimal refundAmount);
}