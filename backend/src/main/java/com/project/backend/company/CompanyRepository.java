package com.project.backend.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository layer for managing Company data.
 *
 * This class acts as an in-memory data store using a List.
 * In a real application, this would typically be replaced
 * with a database-backed repository (e.g. Spring Data JPA).
 */

public class CompanyRepository {


/**
* Internal storage for Company objects.
* 
* ArrayList allows fast access by index.
*/
    private List <Company> companies = new ArrayList<>();

 /**
* Retrieve all companies.
*
* @return a list containing all stored companies
*/
    public List<Company> getCompanies(Company company){
        return companies;
    }

 /**
* Retrieve a single company by its index.
*
* @param index the position of the company in the list
* @return the Company at the given index
*/
    public Company getCompany(int index){
        return  companies.get(index);
    }


/**
* Save a new company.
*
* @param company the Company object to be added
*/
    public void saveCompany(Company company){
        companies.add(company);
    }


/**
* Update an existing company at the given index.
*
* @param index the position of the company to update
* @param company the new Company data
*/
    public void updateCompany(int index, Company company){
        companies.set(index, company);
    }

 /**
* Delete a company at the given index.
*
* @param index the position of the company to remove
*/
    public void deleteCompany(int index){
        companies.remove(index);
    }
};
