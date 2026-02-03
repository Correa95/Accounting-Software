package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.Account;
import com.project.backend.entity.Customer;
import com.project.backend.entity.Invoice;
import com.project.backend.enums.AccountSubType;
import com.project.backend.service.AccountService;
import com.project.backend.service.CustomerService;
import com.project.backend.service.InvoiceService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final AccountService accountService;
    private final CustomerService customerService;


    @GetMapping("/invoice")
    public ResponseEntity<List<Invoice>> getAllInvoices(@PathVariable long companyId) {
        return new ResponseEntity<>(invoiceService.getAllInvoices(companyId), HttpStatus.OK);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable long invoiceId, @PathVariable long companyId) {
        return new ResponseEntity<>(invoiceService.getInvoiceById(invoiceId, companyId), HttpStatus.OK);
    }

    @PostMapping("/customers/{customerId}")
    public ResponseEntity<Invoice> createInvoice(
        @PathVariable Long companyId,
        @PathVariable Long customerId,
        @RequestBody Invoice invoice) {

    Customer customer = customerService.getCustomerById(customerId, companyId);
    invoice.setCustomer(customer);

    Account accountReceivable = accountService.getAccountBySubType(companyId, AccountSubType.ACCOUNTS_RECEIVABLE);
    invoice.setAccount(accountReceivable);
    return new ResponseEntity<>(invoiceService.createInvoice(companyId, customerId, invoice), HttpStatus.CREATED);
    }

    @PutMapping("/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(
        @PathVariable Long companyId,
        @PathVariable Long invoiceId,
        @RequestBody Invoice invoice) {

    Invoice updatedInvoice = invoiceService.updateInvoice(invoiceId, companyId, invoice);
    return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
    }

    @PostMapping("/{invoiceId}/send")
    public ResponseEntity<Invoice> sendInvoice(
            @PathVariable Long companyId,
            @PathVariable Long invoiceId) {
            return new ResponseEntity<>(invoiceService.sendInvoice(invoiceId, companyId), HttpStatus.OK); 
    }

    @PostMapping("/{invoiceId}/void")
    public ResponseEntity<Invoice> voidInvoice(
            @PathVariable Long companyId,
            @PathVariable Long invoiceId) {
            return new ResponseEntity<>(invoiceService.voidInvoice(invoiceId, companyId), HttpStatus.OK);
    }

    @PostMapping("/{invoiceId}/pay")
    public ResponseEntity<Invoice> markPaid(@PathVariable Long companyId, @PathVariable Long invoiceId) {
        return new ResponseEntity<>(invoiceService.markInvoicePaid(invoiceId, companyId), HttpStatus.OK);
    }


    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deactivateInvoice(@PathVariable long invoiceId, @PathVariable long companyId ){
        invoiceService.deactivateInvoice(invoiceId, companyId);
        return  ResponseEntity.noContent().build();
    }

}
    
    

