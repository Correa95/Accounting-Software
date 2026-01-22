package com.project.backend.service;

import com.project.backend.common.enums.AccountSubType;
import com.project.backend.entity.Account;

public interface AccountService {

    Account getAccountType(Long companyId, AccountSubType subType);
}
