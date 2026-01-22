package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.project.backend.common.enums.AccountType;
import com.project.backend.entity.Account;
import com.project.backend.entity.Customer;
import com.project.backend.entity.Invoice;
import com.project.backend.service.AccountService;
import com.project.backend.service.InvoiceService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.backend.common.enums.AccountSubType;
import com.project.backend.service.CustomerService;

@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final AccountService accountService;
    private final CustomerService customerService;

    // Get all invoices for a company
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices(@PathVariable long companyId) {
        return new ResponseEntity<>(invoiceService.getAllInvoices(companyId), HttpStatus.OK);
    }

    //Get a single Invoice
    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable long invoiceId, @PathVariable long companyId) {
        return new ResponseEntity<>(invoiceService.getInvoiceById(invoiceId, companyId), HttpStatus.OK);
    }

    // Create invoice for a specific customer
    @PostMapping("/customers/{customerId}")
    public ResponseEntity<Invoice> createInvoice(
        @PathVariable Long companyId,
        @PathVariable Long customerId,
        @RequestBody Invoice invoice) {
        // Attach customer (company ownership enforced)
    Customer customer = customerService.getCustomerById(customerId, companyId);
    invoice.setCustomer(customer);
    // Assign Accounts Receivable automatically
    Account accountReceivable = accountService.getAccountType(companyId, AccountSubType.ACCOUNTS_RECEIVABLE);
    invoice.setAccount(accountReceivable);
    return new ResponseEntity<>(invoiceService.createInvoice(invoice, companyId, customerId), HttpStatus.CREATED);
    }

    // Update invoice (business rules enforced in service)
    @PutMapping("/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(
        @PathVariable Long companyId,
        @PathVariable Long invoiceId,
        @RequestBody Invoice invoice) {

    Invoice updatedInvoice = invoiceService.updateInvoice(invoiceId, companyId, invoice);
    return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
}


    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deactivateInvoice(@PathVariable long invoiceId, @PathVariable long companyId ){
        invoiceService.deactivateInvoice(invoiceId, companyId);
        return  ResponseEntity.noContent().build();
    }

}
    
    

