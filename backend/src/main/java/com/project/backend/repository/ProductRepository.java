package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
   // Return all active products for a company
    List<Product> findByCompanyIdAndActiveTrue(Long companyId);


     // Return a specific product for a company 
    Optional<Product> findByIdAndCompanyId(Long productId, Long companyId);
}
