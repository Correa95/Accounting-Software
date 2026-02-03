package com.project.backend.extra;


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

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("companies/{companyId}/invoices/{invoiceId}/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments(@PathVariable long companyId, @PathVariable long invoiceId) {
        return new ResponseEntity<>(paymentService.getAllPayments(companyId), HttpStatus.OK);
    }


    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable long companyId, @PathVariable long invoiceId,@PathVariable long paymentId) {
    return new ResponseEntity<>(paymentService.getPayment(paymentId, companyId), HttpStatus.OK);

    }


    @PostMapping
    public ResponseEntity<Payment> createPayment(@PathVariable long companyId, @PathVariable long invoiceId, @RequestBody Payment payment) {
        return new ResponseEntity<>(paymentService.createPayment(companyId, invoiceId, payment), HttpStatus.CREATED);
    }


    @PutMapping("/{paymentId}")
    public ResponseEntity<Payment> updatePayment(@PathVariable long companyId, @PathVariable long invoiceId, @PathVariable long paymentId, @RequestBody Payment payment) {
        return new ResponseEntity<>(paymentService.updatePayment(paymentId, companyId, payment), HttpStatus.OK);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deactivatePayment(@PathVariable long companyId,  @PathVariable long invoiceId, @PathVariable long paymentId) {
        paymentService.deactivatePayment(paymentId, companyId);
        return ResponseEntity.noContent().build();
    }
}
