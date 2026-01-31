package com.project.backend.service;

import com.project.backend.entity.Account;
import com.project.backend.enums.AccountSubType;

public interface AccountService {

    Account getAccountType(Long companyId, AccountSubType subType);
}
