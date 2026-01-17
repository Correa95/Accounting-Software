package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Company;
import com.project.backend.repository.CompanyRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService{

    public final CompanyRepository companyRepository;

    @Override
    public List<Company> getCompanies(){
        return companyRepository.findAll();
    }

    @Override
    public Company getCompany(long id){
        return companyRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Override
    public Company saveCompany(Company company){
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(long id, Company company) {
    Company existingCompany = getCompany(id);
    existingCompany.setName(company.getName());
    existingCompany.setLegalName(company.getLegalName());
    existingCompany.setAddress(company.getAddress());
    existingCompany.setPhone(company.getPhone());
    existingCompany.setEmail(company.getEmail());
    existingCompany.setTaxId(company.getTaxId());
    existingCompany.setCurrencyCode(company.getCurrencyCode());
    existingCompany.setFiscalYearStart(company.getFiscalYearStart());
    existingCompany.setFiscalYearEnd(company.getFiscalYearEnd());
    return companyRepository.save(existingCompany);
}

    @Override
    public void deleteCompany(long id){
        companyRepository.deleteById(id);
    }
}
