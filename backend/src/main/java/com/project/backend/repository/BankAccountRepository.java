package com.project.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stripe.model.BankAccount;



@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    // Get all active bank accounts for a specific company
    List<BankAccount> findByCompanyIdAndActiveTrue(Long companyId);

}

