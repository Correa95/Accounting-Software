package com.project.backend.company;

import java.util.List;

/**
 * Service interface for Company-related operations.
 *
 * This interface defines the business operations that can be
 * performed on Company entities.
 *
 * The implementation (CompanyServiceImplements) will contain
 * the actual logic, while controllers depend only on this interface.
 */

public interface CompanyService{
    
    /**
     * Retrieve a company by its unique ID.
     *
     * @param id the unique identifier of the company
     * @return the Company with the given ID
     * @throws RuntimeException if the company is not found
     */
    Company getCompanyById(String id);

    /**
     * Save a new company.
     *
     * @param company the Company object to be saved
     */
    void saveCompany(Company company);

    /**
     * Update an existing company.
     *
     * @param id the unique identifier of the company to update
     * @param company the updated company data
     */
    void updateCompany(String index, Company company);

    /**
     * Delete a company by its unique ID.
     *
     * @param id the unique identifier of the company to delete
     */
    void deleteCompany(String index);

    /**
     * Retrieve all companies.
     *
     * @return a list of all Company objects
     */
    List<Company> getCompanies();
}
