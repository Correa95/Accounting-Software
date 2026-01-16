package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Customer;

@Service
public interface  CustomerService {
    List<Customer> getCustomers(long companyId);
    Customer getCustomer(Long customerId, long companyId);
    Customer savCustomer(Customer customer, long companyId);
    Customer updateCustomer(long customerId, long companyId, Customer customer);
    void deleteCustomer(Long customerId, long companyId);

}
