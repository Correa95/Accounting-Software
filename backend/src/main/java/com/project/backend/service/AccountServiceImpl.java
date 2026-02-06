package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.Account;
import com.project.backend.entity.Company;
import com.project.backend.enums.AccountSubType;
import com.project.backend.enums.AccountType;
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
    public Account getOrCreateAccountBySubType(long companyId, AccountSubType subType) {
        return accountRepository.findByCompanyIdAndAccountSubTypeAndActiveTrue(companyId, subType)
                .orElseGet(() -> {
                    Company company = companyService.getCompanyById(companyId);
                    Account account = new Account();
                    account.setCompany(company);
                    account.setAccountSubType(subType);
                    account.setAccountType(mapSubTypeToType(subType));
                    account.setName(subType.name().replace("_", " "));
                    account.setActive(true);
                    return accountRepository.save(account);
                });
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
    public Account updateAccount(long accountId, Account account) {
        Account existing = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        existing.setName(account.getName());
        existing.setAccountNumber(account.getAccountNumber());
        existing.setAccountType(account.getAccountType());
        existing.setAccountSubType(account.getAccountSubType());
        existing.setDescription(account.getDescription());
        return accountRepository.save(existing);
    }

    @Override
    public void deactivateAccount(long accountId) {
        Account existing = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        existing.setActive(false);
        accountRepository.save(existing);
    }

    private AccountType mapSubTypeToType(AccountSubType subType) {
    // Simple mapping, extend as needed
    return switch (subType) {
        case CASH, BANK, ACCOUNTS_RECEIVABLE, INVENTORY, FIXED_ASSETS -> AccountType.ASSET;
        case ACCOUNTS_PAYABLE, TAX_PAYABLE -> AccountType.LIABILITY;
        case SALES_REVENUE, SERVICE_REVENUE -> AccountType.REVENUE;
        case OPERATING_EXPENSE, SALARY_EXPENSE -> AccountType.EXPENSE;
        default -> AccountType.OTHER;
    };
}


}
