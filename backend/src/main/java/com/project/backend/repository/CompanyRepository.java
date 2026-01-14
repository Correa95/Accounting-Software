package com.project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.project.backend.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{

}