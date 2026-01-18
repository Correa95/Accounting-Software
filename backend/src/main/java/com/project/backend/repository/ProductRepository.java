package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    // This find the Product associated with the company
    List<Product> findById(long companyId);

    // Optional is used because the Product might not exist or not belong to the this specific company
    Optional<Product> findByIdAndCompanyId(long id, long companyId);
}
