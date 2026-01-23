package com.project.backend.service;

import java.util.List;
import com.project.backend.entity.BankTransaction;

public interface BankTransactionService {
    List<BankTransaction> getAllBankTransactions(long companyId);
    BankTransaction getBankTransaction(long bankTransactionId, long companyId);
    BankTransaction createBankTransaction(BankTransaction bankTransaction, long companyId);
    BankTransaction updateBankTransaction(long bankTransactionId, long companyId, BankTransaction bankTransaction);
    void deactivateBankTransaction(long bankTransactionId, long companyId);
}
