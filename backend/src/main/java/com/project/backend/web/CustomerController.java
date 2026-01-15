package com.project.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.service.CompanyService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;


@AllArgsConstructor
@RestController
@RequestMapping("/customer")
public class CustomerController {

    public final CompanyService companyService;

    @GetMapping("path")
    public ResponseEntity<List<Customer> getCustomers() {
        return 
    }
    




    

    
}
