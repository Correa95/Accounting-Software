package com.project.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.JournalEntryLine;

@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {

    // Used by JournalEntryLineService
    List<JournalEntryLine> findByJournalEntryIdAndCompanyId(long journalEntryId, long companyId);

    // Used by TrialBalanceService — all time
    List<JournalEntryLine> findByCompany_Id(long companyId);

    // Used by TrialBalanceService — date range
    List<JournalEntryLine> findByCompany_IdAndJournalEntry_EntryDateBetween(
            long companyId, LocalDate startDate, LocalDate endDate);
}