package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.project.backend.entity.Company;
import com.project.backend.entity.Customer;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.CustomerRepository;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<Customer> getCustomers(Long companyId) {
        return customerRepository.findByCompanyId(companyId);
    }

    @Override
    public Customer getCustomer(Long customerId, Long companyId) {
        return customerRepository
                .findByIdAndCompanyId(customerId, companyId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Customer saveCustomer(Customer customer, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        customer.setCompany(company);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long customerId, Long companyId, Customer customer) {
        Customer existingCustomer = getCustomer(customerId, companyId);

        existingCustomer.setName(customer.getName());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setBillingAddress(customer.getBillingAddress());
        existingCustomer.setShippingAddress(customer.getShippingAddress());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId, Long companyId) {
        Customer customer = getCustomer(customerId, companyId);
        customerRepository.delete(customer);
    }
}
