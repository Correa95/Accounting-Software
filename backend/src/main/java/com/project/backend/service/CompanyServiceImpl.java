package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.common.enums.BusinessType;
import com.project.backend.entity.Company;
import com.project.backend.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
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
        validateBusinessType(company.getBusinessType());
        company.setActive(true);
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(long id, Company company) {
    Company existingCompany = getCompany(id);
    
    
        if (company.getName() != null)
            existingCompany.setName(company.getName());

        if (company.getLegalName() != null)
            existingCompany.setLegalName(company.getLegalName());

        if (company.getAddress() != null)
            existingCompany.setAddress(company.getAddress());

        if (company.getPhone() != null)
            existingCompany.setPhone(company.getPhone());

        if (company.getEmail() != null)
            existingCompany.setEmail(company.getEmail());

        if (company.getTaxId() != null)
            existingCompany.setTaxId(company.getTaxId());

        if (company.getCurrencyCode() != null)
            existingCompany.setCurrencyCode(company.getCurrencyCode());

        if (company.getFiscalYearStart() != null)
            existingCompany.setFiscalYearStart(company.getFiscalYearStart());

        if (company.getFiscalYearEnd() != null)
            existingCompany.setFiscalYearEnd(company.getFiscalYearEnd());

        if (company.getBusinessType() != null) {
            validateBusinessType(company.getBusinessType());
            existingCompany.setBusinessType(company.getBusinessType());
        }


    return companyRepository.save(existingCompany);
}

    @Override
    public void deleteCompany(long id){
        Company company = getCompany(id);
        company.setActive(true);
        companyRepository.save(company);
    }

    
    private void validateBusinessType(BusinessType businessType) {
        if (businessType == null) {
            throw new IllegalArgumentException("Business type must be selected");
        }
    }
}
