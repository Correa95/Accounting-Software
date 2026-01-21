package com.project.backend.web;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.common.enums.AccountType;
import com.project.backend.entity.Account;
import com.project.backend.entity.Invoice;
import com.project.backend.service.AccountService;
import com.project.backend.service.InvoiceService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("companies/{companyId}/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final AccountService accountService;
// Get all invoices
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices(@PathVariable long companyId) {
        return new ResponseEntity<>(invoiceService.getAllInvoices(companyId), HttpStatus.OK);
    }
    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable long invoiceId, @PathVariable long companyId) {
        return new ResponseEntity<>(invoiceService.getInvoiceById(invoiceId, companyId), HttpStatus.OK);
    }
    // Create invoice
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(
        @PathVariable Long companyId,
        @RequestBody Invoice invoice) {

//     // Optional: fetch AR account for the company automatically
//     Account accountReceivable = accountService.getAccountByType(companyId, AccountType.ACCOUNTS_RECEIVABLE);
//     invoice.setAccount(accountReceivable);

//     Invoice createdInvoice = invoiceService.createInvoice(invoice, companyId);
//     return new ResponseEntity<>(invoiceService.createInvoice(invoice, companyId), HttpStatus.CREATED);
//     // return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
//     }

    // @PutMapping("/{invoiceId}")
    // public ResponseEntity<Invoice> updateInvoice(
    //     @PathVariable Long companyId,
    //     @PathVariable Long invoiceId,
    //     @RequestBody Invoice invoice) {

    // // Fetch the existing invoice
    // Invoice existingInvoice = invoiceService.getInvoiceById(invoiceId, companyId);

    // // Update basic fields
    // existingInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
    // existingInvoice.setInvoiceDate(invoice.getInvoiceDate());
    // existingInvoice.setInvoiceDueDate(invoice.getInvoiceDueDate());
    // existingInvoice.setInvoiceAmount(invoice.getInvoiceAmount());
    // existingInvoice.setInvoiceStatus(invoice.getInvoiceStatus());
    // existingInvoice.setActive(invoice.isActive());

    // // Optional: Only update AR account if explicitly passed
    // if (invoice.getAccount() != null) {
    //     existingInvoice.setAccount(invoice.getAccount());
    // }
    // return ResponseEntity<>(invoiceService.updateInvoice(existingInvoice, companyId), HttpStatus.OK);
    // }

    // @DeleteMapping("/{invoiceId}")
    // public ResponseEntity<Void> deactivateInvoice(@PathVariable long invoiceId, @PathVariable long companyId ){

    //     invoice.setInvoice(false);
    //     invoiceService.deactivateInvoice(invoiceId, companyId)
    //     return ResponseEntity.onContent.build();
    // }

}
    
    

