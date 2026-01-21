package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.BankTransaction;
import com.project.backend.entity.Company;
import com.project.backend.repository.BankTransactionRepository;
import com.project.backend.repository.CompanyRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankTransactionServiceImpl implements BankTransactionService {

    private final BankTransactionRepository bankTransactionRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<BankTransaction> getAllBankTransactions(Long companyId) {
        return bankTransactionRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public BankTransaction getBankTransaction(Long bankTransactionId, Long companyId) {
        return bankTransactionRepository.findByIdAndCompanyIdAndActiveTrue(bankTransactionId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("BankTransaction not found"));
    }

    @Override
    public BankTransaction createBankTransaction(BankTransaction bankTransaction, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        bankTransaction.setCompany(company);
        bankTransaction.setActive(true);
        return bankTransactionRepository.save(bankTransaction);
    }

    @Override
    public BankTransaction updateBankTransaction(Long transactionId, Long companyId, BankTransaction bankTransaction) {
        BankTransaction existing = getBankTransaction(transactionId, companyId);
        if (bankTransaction.getTransactionAmount() != null) existing.setTransactionAmount(bankTransaction.getTransactionAmount());
        if (bankTransaction.getTransactionDate() != null) existing.setTransactionDate(bankTransaction.getTransactionDate());
        if (bankTransaction.getTransactionType() != null) existing.setTransactionType(bankTransaction.getTransactionType());
        if (bankTransaction.getBankReferenceNumber() != null) existing.setBankReferenceNumber(bankTransaction.getBankReferenceNumber());
        return bankTransactionRepository.save(existing);
    }

    @Override
    public void deactivateBankTransaction(Long bankTransactionId, Long companyId) {
        BankTransaction bankTransaction = getBankTransaction(bankTransactionId, companyId);
        bankTransaction.setActive(false);
        bankTransactionRepository.save(bankTransaction);
    }
}
