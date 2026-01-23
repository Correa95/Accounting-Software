package com.project.backend.service;


import java.util.List;

import com.project.backend.entity.BankAccount;

public interface BankAccountService {

    List<BankAccount> getBankAccountsByCompany(Long companyId);
    BankAccount createBankAccount(BankAccount account);

    BankAccount updateBankAccount(Long accountId, BankAccount account);


    void deactivateBankAccount(Long accountId);
}

