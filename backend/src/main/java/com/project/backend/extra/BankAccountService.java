package com.project.backend.extra;


import java.util.List;

public interface BankAccountService {

    List<BankAccount> getBankAccountsByCompany(Long companyId);
    BankAccount createBankAccount(BankAccount account);

    BankAccount updateBankAccount(Long accountId, BankAccount account);


    void deactivateBankAccount(Long accountId);
}

