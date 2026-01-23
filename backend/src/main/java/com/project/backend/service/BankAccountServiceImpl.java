package com.project.backend.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.BankAccount;
import com.project.backend.repository.BankAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// @RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Override
    public List<BankAccount> getBankAccountsByCompany(Long companyId) {
        return bankAccountRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public BankAccount createBankAccount(BankAccount account) {
        return bankAccountRepository.save(account);
    }

    @Override
    public BankAccount updateBankAccount(Long accountId, BankAccount bankAccount) {
        BankAccount existing = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Bank account not found"));
        existing.setBankName(bankAccount.getBankName());
        existing.setAccountNumber(bankAccount.getAccountNumber());
        existing.setAccountType(bankAccount.getAccountType());
        return bankAccountRepository.save(existing);
    }


    @Override
    public void deactivateBankAccount(Long accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Bank account not found"));
        account.setActive(false);
        bankAccountRepository.save(account);
    }
}
