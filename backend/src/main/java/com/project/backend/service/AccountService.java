package com.project.backend.service;

import java.math.BigDecimal;
import java.util.List;

import com.project.backend.entity.Account;
import com.project.backend.enums.AccountSubType;

public interface AccountService {

    Account getAccountById(long accountId);

    Account getAccountBySubType(long companyId, AccountSubType subType);

    Account getOrCreateAccountBySubType(long companyId, AccountSubType accountSubType);

    List<Account> getAllAccounts(long companyId);

    Account createAccount(long companyId, Account account);

    Account updateAccount(long accountId, Account account);

    void deactivateAccount(long accountId);

    BigDecimal getAccountBalance(long accountId);
}