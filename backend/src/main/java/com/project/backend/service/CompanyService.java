package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Company;
@Service
public interface  CompanyService {
  
    List<Company> getCompanies();
    Company getCompany(long id);
    Company saveCompany(Company company);
    Company updateCompany(Long id, Company company);
    void deleteCompany(Long id);

    
    
}
