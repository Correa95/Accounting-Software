package com.project.backend.service;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Account;
import com.project.backend.enums.AccountSubType;
import com.project.backend.repository.AccountRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccountType(Long companyId, AccountSubType subType) {
        return accountRepository
                .findByCompanyIdAndAccountSubTypeAndActiveTrue(companyId, subType)
                .orElseThrow(() -> new IllegalStateException("Account of type " + subType + " not found for company " + companyId));
    }
}
