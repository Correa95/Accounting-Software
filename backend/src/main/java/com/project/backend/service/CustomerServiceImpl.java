package com.project.backend.service;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
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
    



}
