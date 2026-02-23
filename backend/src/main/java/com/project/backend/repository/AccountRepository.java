package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.backend.entity.Account;
import com.project.backend.enums.AccountSubType;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCompanyIdAndAccountSubTypeAndActiveTrue(Long companyId, AccountSubType accountSubType);

    List<Account> findByCompanyIdAndActiveTrue(Long companyId);

    // Fetch account with all its lines in one query — avoids N+1 when computing balance
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.journalEntryLines WHERE a.id = :accountId")
    Optional<Account> findByIdWithLines(Long accountId);
}