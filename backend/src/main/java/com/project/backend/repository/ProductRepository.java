package com.project.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findById(long companyId);
}
