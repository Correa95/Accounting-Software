package com.project.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.common.enums.AccountSubType;
import com.project.backend.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCompanyIdAndAccountSubTypeAndActiveTrue(
            Long companyId,
            AccountSubType accountSubType
    );

}
