package com.project.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.JournalEntryLine;

@Repository
public interface JournalEntryLineRepository
        extends JpaRepository<JournalEntryLine, Long> {

    List<JournalEntryLine> findByCompany_IdAndActiveTrue(
            long companyId
    );

    List<JournalEntryLine> findByJournalEntry_IdAndCompany_Id(
            long journalEntryId,
            long companyId
    );

    // ✅ Example date-based query (CORRECT way)
    // Uses JournalEntry.entryDate
    List<JournalEntryLine> findByCompany_IdAndJournalEntry_EntryDateBetween(
            long companyId,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    );
}
