package com.project.backend.service;

import com.project.backend.entity.Account;
import com.project.backend.entity.Invoice;
import com.project.backend.enums.AccountSubType;

public interface AccountService {

    Account getAccountBySubType(long companyId, AccountSubType subType);
    Account postAccount(long companyId, Invoice invoice);
}
