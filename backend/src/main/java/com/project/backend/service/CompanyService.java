package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Company;

@Service
public interface  CompanyService {
  
    List<Company> getCompanies();
    Company getCompanyById(long id);
    Company saveCompany(Company company);
    Company updateCompany(long id, Company company);
    void deleteCompany(long id);

    
    
}
