package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.BankTransaction;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {
    List<BankTransaction> findByCompanyIdAndActiveTrue(Long companyId);
    Optional<BankTransaction> findByIdAndCompanyIdAndActiveTrue(Long BankTransactionId, Long companyId);
}
