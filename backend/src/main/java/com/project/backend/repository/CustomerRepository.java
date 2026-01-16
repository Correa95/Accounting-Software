package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Customer;

public interface  CustomerRepository extends JpaRepository<Customer, Long>{
    // This find the customer associated with the company
    List<Customer> findByCompanyId(Long companyId);
    // Optional is used because the customer might not exist or not belong to the this specific company
    Optional<Customer> findByIdAndCompanyId(Long id, Long companyId);
    
}
