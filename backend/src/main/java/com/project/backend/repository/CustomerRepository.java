package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Customer;

public interface  CustomerRepository extends JpaRepository<Customer, Long>{
    List<Customer> findByCompanyId(Long companyId);
    Optional<Customer> findByIdAndCompanyId(Long id, Long companyId);
    
}
