package com.project.backend.service;

import com.project.backend.entity.Account;
import com.project.backend.enums.AccountSubType;

public interface AccountService {

    Account getAccountBySubType(Long companyId, AccountSubType subType);
}
