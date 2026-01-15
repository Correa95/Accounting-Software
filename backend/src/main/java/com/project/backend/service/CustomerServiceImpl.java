package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.AllArgsConstructor;

import com.project.backend.entity.Company;
import com.project.backend.entity.Customer;
import com.project.backend.repository.CustomerRepository;


@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    public final CustomerRepository customerRepository;


    @Override
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long id){
        return customerRepository.findById(id);
    }

    @Override
    public Customer savCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    @Override
    public Customer updaCustomer(long id, Customer customer){
        Customer existingCustomer = getCustomer(id);
Company existingCustomer = getCompany(id);
    existingCustomer.setName(name.getName());
    existingCustomer.setAddress(customer.getAddress());
    existingCustomer.setPhone(customer.getPhone());
    existingCustomer.setEmail(customer.getEmail());
    existingCustomer.setTaxId(customer.getTaxId());


    existingCustomer.setFiscalPeriod(customer.getFiscalPeriod());
    return companyRepository.save(existingCustomer);
    }




}
