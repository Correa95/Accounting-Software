package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{

    List<Company> findByActiveTrue();

    Optional<Company> findByIdAndActiveTrue(Long id);

}