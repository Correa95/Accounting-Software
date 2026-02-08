package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Invoice;
import com.project.backend.entity.PaymentOrder;

public interface InvoiceService {

    List<Invoice> getAllInvoices(long companyId);

    Invoice getInvoiceById(long invoiceId, long companyId);

    Invoice createInvoice(long companyId, long customerId, Invoice invoice);

    Invoice updateInvoice(long invoiceId, long companyId, Invoice invoice);

    void deactivateInvoice(long invoiceId, long companyId);
    
    Invoice sendInvoice(long invoiceId, long companyId);

    Invoice voidInvoice(long invoiceId, long companyId);


    Invoice markInvoicePaid(PaymentOrder payment);

}
