package com.project.backend.company;

import java.util.ArrayList;
import java.util.List;

public class CompanyRepository {
    
    private List <Company> companies = new ArrayList<>();

    public List<Company> getAllCompanies(Company company){
        return companies;
    }

    public Company getCompanyByIndex(int index){
        // return  companies.get(index);
    }

    public Company saveCompany(Company company){
        companies.add(company);
        return company;
    }

    public void updateCompany(int index, Company company){
        companies.set(index, company);
    }
};
