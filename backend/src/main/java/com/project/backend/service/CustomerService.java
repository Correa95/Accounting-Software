package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Customer;

@Service
public interface  CustomerService {
    List<Customer> getCustomers();
    Customer getCustomer(Long id);
    Customer savCustomer(Customer customer);
    Customer updateCustomer(long id, Customer customer);
    void deleteCustomer(Long id);

}
