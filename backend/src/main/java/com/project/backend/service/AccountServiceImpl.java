package com.project.backend.service;

import java.math.BigDecimal;
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
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CompanyService companyService;

    @Override
    @Transactional(readOnly = true)
    public Account getAccountById(long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountBySubType(long companyId, AccountSubType subType) {
        return accountRepository
                .findByCompanyIdAndAccountSubTypeAndActiveTrue(companyId, subType)
                .orElseThrow(() -> new IllegalStateException(
                        "Active account not found: " + subType + " for company " + companyId));
    }

    @Override
    public Account getOrCreateAccountBySubType(long companyId, AccountSubType subType) {
        return accountRepository
                .findByCompanyIdAndAccountSubTypeAndActiveTrue(companyId, subType)
                .orElseGet(() -> {
                    Company company = companyService.getCompanyById(companyId);
                    Account account = new Account();
                    account.setCompany(company);
                    account.setAccountSubType(subType);
                    account.setAccountType(mapSubTypeToType(subType));
                    account.setAccountName(subType.name().replace("_", " "));
                    account.setAccountNumber("ACC-" + System.currentTimeMillis());
                    account.setActive(true);
                    return accountRepository.save(account);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts(long companyId) {
        return accountRepository.findByCompanyIdAndActiveTrue(companyId);
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
        Account existing = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        existing.setAccountName(account.getAccountName());
        existing.setAccountNumber(account.getAccountNumber());
        existing.setAccountType(account.getAccountType());
        existing.setAccountSubType(account.getAccountSubType());
        existing.setDescription(account.getDescription());
        return accountRepository.save(existing);
    }

    @Override
    public void deactivateAccount(long accountId) {
        Account existing = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        existing.setActive(false);
        accountRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(long accountId) {
        Account account = accountRepository.findByIdWithLines(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        return account.getComputedBalance();
    }

    // ─────────────────────────────────────
    // INTERNAL HELPERS
    // ─────────────────────────────────────

    private AccountType mapSubTypeToType(AccountSubType subType) {
        return switch (subType) {
            case CASH, BANK, ACCOUNTS_RECEIVABLE, INVENTORY, FIXED_ASSETS -> AccountType.ASSET;
            case ACCOUNTS_PAYABLE, TAX_PAYABLE -> AccountType.LIABILITY;
            case SALES_REVENUE, SERVICE_REVENUE -> AccountType.REVENUE;
            case OPERATING_EXPENSE, SALARY_EXPENSE -> AccountType.EXPENSE;
            default -> AccountType.OTHER;
        };
    }
}