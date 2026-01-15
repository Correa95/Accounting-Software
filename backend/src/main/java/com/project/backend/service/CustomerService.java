package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Customer;


public interface  CustomerService {
    List<Customer> getCustomers();
    Customer getCustomer(Long id);
    Customer savCustomer(Customer customer);
    void deleteCustomer(Long id);
}
