package com.project.backend.repository;
import java.util.List;
import java.util.Optional;

import  org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.JournalEntry;
import com.project.backend.entity.PaymentOrder;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByCompanyIdAndDeletedFalse(long companyId);

    Optional<JournalEntry> findByIdAndCompanyIdAndDeletedFalse(long journalEntryId, long companyId);
    List<PaymentOrder> findByPayment(PaymentOrder payment);
}
