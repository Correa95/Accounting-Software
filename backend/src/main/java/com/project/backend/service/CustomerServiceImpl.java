package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Company;
import com.project.backend.entity.Customer;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.CustomerRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<Customer> getAllCustomers(long companyId) {
        // This find the customer associated with the company
        return customerRepository.findByCompanyId(companyId);
    }

    @Override
    public Customer getCustomerById(long customerId, long companyId) {
        // find the customer that is only associated with this company
        return customerRepository
                .findByIdAndCompanyId(customerId, companyId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Customer saveCustomer(Customer customer, long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

                
        customer.setCompany(company);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(long customerId, long companyId, Customer customer) {
        Customer existingCustomer = getCustomerById(customerId, companyId);

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setBillingAddress(customer.getBillingAddress());
        existingCustomer.setShippingAddress(customer.getShippingAddress());
        existingCustomer.setPaymentTerm(customer.getPaymentTerm());
        existingCustomer.setCreditLimit(customer.getCreditLimit());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(long customerId, long companyId) {
        Customer customer = getCustomerById(customerId, companyId);
        customer.setActive(false);
        customerRepository.save(customer);
    }
}
