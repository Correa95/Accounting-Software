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
    Company existing = getCompany(id);
    existing.setName(name.getName());
    existing.setLegalName(company.getLegalName());
    existing.setAddress(company.getAddress());
    existing.setPhone(company.getPhone());
    existing.setEmail(company.getEmail());
    existing.setTaxId(company.getTaxId());
    existing.setCurrencyCode(company.getCurrencyCode());
    existing.setFiscalPeriod(company.getFiscalPeriod());
    return companyRepository.save(existing);
}

    @Override
    public void deleteCompany(Long id){
        companyRepository.deleteById(id);
    }
}
