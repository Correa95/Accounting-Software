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
        return CompanyRepository.findAll();
    }

    @Override
    public Company getCompany(Long id){
        return companyRepository.findById(id);
    }

    @Override
    public Company saveCompany(Company company){
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Long id, Company company) {
    Company existingCompany = getCompany(id);
    existingCompany.setName(name.getName());
    existingCompany.setLegalName(company.getLegalName());
    existingCompany.setAddress(company.getAddress());
    existingCompany.setPhone(company.getPhone());
    existingCompany.setEmail(company.getEmail());
    existingCompany.setTaxId(company.getTaxId());
    existingCompany.setCurrencyCode(company.getCurrencyCode());
    existingCompany.setFiscalPeriod(company.getFiscalPeriod());
    return companyRepository.save(existingCompany);
}

    @Override
    public void deleteCompany(Long id){
        companyRepository.deleteById(id);
    }
}
