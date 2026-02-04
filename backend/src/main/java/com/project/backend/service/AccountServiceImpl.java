package com.project.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.Account;
import com.project.backend.enums.AccountSubType;
import com.project.backend.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccountBySubType(long companyId, AccountSubType accountSubType) {
        return accountRepository.findByCompanyIdAndAccountSubTypeAndActiveTrue(companyId, accountSubType).orElseThrow(() ->new IllegalStateException("Active account not found: " + accountSubType + " for company " + companyId));
    }
}
