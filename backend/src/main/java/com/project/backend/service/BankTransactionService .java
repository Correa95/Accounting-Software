package com.project.backend.service;

import java.util.List;
import com.project.backend.entity.BankTransaction;

public interface BankTransactionService {
    List<BankTransaction> getAllBankTransactions(Long companyId);
    BankTransaction getBankTransaction(Long bankTransactionId, Long companyId);
    BankTransaction createBankTransaction(BankTransaction bankTransaction, Long companyId);
    BankTransaction updateBankTransaction(Long bankTransactionId, Long companyId, BankTransaction bankTransaction);
    void deactivateBankTransaction(Long bankTransactionId, Long companyId);
}
