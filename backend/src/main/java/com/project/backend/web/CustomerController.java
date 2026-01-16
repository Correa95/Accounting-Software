package com.project.backend.web;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.Customer;
import com.project.backend.service.CompanyService;
import com.project.backend.service.CustomerService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;






@AllArgsConstructor
@RestController
@RequestMapping("/companies/{companyId}/customers")
public class CustomerController {

    private final CustomerService customerService;

  // Get all customers for a company
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(@PathVariable long companyId) {
        return new ResponseEntity<>(customerService.getCustomers(companyId), HttpStatus.OK);
    }

    // Get a single customer
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long companyId, @PathVariable long customerId) {
        return new ResponseEntity<>(customerService.getCustomer(customerId, companyId), HttpStatus.OK);
    }

     // Create a customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer, @PathVariable long companyId) {
        return new ResponseEntity<>(customerService.saveCustomer(customer, companyId), HttpStatus.CREATED);
    }
    // Update a customer
    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable long companyId, @PathVariable long customerId, @RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.updateCustomer(customerId, companyId, customer), HttpStatus.OK);
    }

    // Delete a customer
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(
        @PathVariable Long companyId,
        @PathVariable Long customerId) {

    customerService.deleteCustomer(customerId, companyId);
    return new ResponseEntity<>(customerService.deleteCustomer(companyId, customerId), HttpStatus.NO_CONTENT); // HTTP 204
}
        



}
    


    
    




    

    
}
