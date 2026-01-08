package com.project.backend.company;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for Company-related business logic.
 * 
 * This class sits between the Controller and Repository.
 * It handles validation, lookup logic, and delegates data operations
 * to the CompanyRepository.
 */

@Service
public class CompanyServiceImplements implements CompanyService {
/**
 * Repository that manages Company data.
* 
* Spring injects (creates and provides) this dependency automatically
* using @Autowired.
*/
    @Autowired
    private CompanyRepository companyRepository;


/**
* Retrieve all companies.
* 
* @return a list of all Company objects
*/
    @Override
    public List<Company> getCompanies(){
         // Return the full list from the repository
        return companyRepository.getCompanies();
    }

/**
* Find and return a Company by its ID.
* 
* @param id the unique identifier of the company
* @return the matching Company
* @throws RuntimeException if the company is not found
*/
    @Override
    public Company getCompanyById(String id){
// Find the index of the company and return it from the repository
        return companyRepository.getCompany(findIndexById(id));
    }

/**
* Save a new Company.
* 
* @param company the company object to save
*/    
    @Override
    public void saveCompany(Company company){
        // Delegate save logic to the repository
        companyRepository.saveCompany(company);
    }
    
/**
* Update an existing Company.
* 
* @param id the ID of the company to update
* @param company the updated company data
*/
    @Override
    public void updateCompany(String id, Company company){
        // Find the company index and update it
        companyRepository.updateCompany(findIndexById(id), company);
    }


 /**
* Delete a Company by ID.
* 
* @param id the ID of the company to delete
*/    
    @Override
    public void deleteCompany(String id){
        // Find the company index and remove it
        companyRepository.deleteCompany(findIndexById(id));
    }


 /**
* Find the index of a Company in the list by its ID.
* 
* This method uses an IntStream so we can:
* - iterate over list indexes
* - return the index instead of the object
* 
* @param id the ID to search for
* @return the index of the company in the list
* @throws RuntimeException if no company with the given ID exists
     */
    private int findIndexById(String id){
        return IntStream.range(0, companyRepository.getCompanies().size())
        // Keep only indexes where the company ID matches
        .filter(index -> companyRepository.getCompanies().get(index).getId().equals(id))
        // Return the first matching index
        .findFirst()
        .orElseThrow(()-> new RuntimeException("Company Not Found"));
    }  
}
