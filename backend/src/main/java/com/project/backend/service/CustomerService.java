package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Customer;


public interface  CustomerService {
    List<Customer> getAllCustomers(long companyId);
    Customer getCustomerById(long customerId, long companyId);
    Customer saveCustomer(Customer customer, long companyId);
    Customer updateCustomer(long customerId, long companyId, Customer customer);
    void deactivateCustomer(long customerId, long companyId);

}
