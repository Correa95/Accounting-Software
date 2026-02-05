package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.Account;
import com.project.backend.entity.Company;
import com.project.backend.enums.AccountSubType;
import com.project.backend.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CompanyService companyService;

    @Override
    @Transactional(readOnly = true)
    public Account getAccountBySubType(long companyId, AccountSubType subType) {
        return accountRepository.findByCompanyIdAndAccountSubTypeAndActiveTrue(companyId, subType)
                .orElseThrow(() -> new IllegalStateException(
                        "Active account not found: " + subType + " for company " + companyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts(long companyId) {
        Company company = companyService.getCompanyById(companyId);
        return company.getAccounts();
    }

    @Override
    public Account createAccount(long companyId, Account account) {
        Company company = companyService.getCompanyById(companyId);
        account.setCompany(company);
        account.setActive(true);
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(long accountId, Account updatedAccount) {
        Account existing = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        existing.setName(updatedAccount.getName());
        existing.setAccountNumber(updatedAccount.getAccountNumber());
        existing.setAccountType(updatedAccount.getAccountType());
        existing.setAccountSubType(updatedAccount.getAccountSubType());
        existing.setDescription(updatedAccount.getDescription());
        return accountRepository.save(existing);
    }

    @Override
    public void deactivateAccount(long accountId) {
        Account existing = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        existing.setActive(false);
        accountRepository.save(existing);
    }
}
