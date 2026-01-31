package com.project.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.JournalEntryLine;

@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {

    // Get all active lines for a company
    List<JournalEntryLine> findByCompany_IdAndActiveTrue(Long companyId);

    // Get all active lines for a company within a date range
    List<JournalEntryLine> findByCompany_IdAndActiveTrueAndEntryDateBetween(
            Long companyId, LocalDate startDate, LocalDate endDate);

    // Get all lines for a specific journal entry within a company
    List<JournalEntryLine> findByJournalEntry_IdAndCompany_Id(Long journalEntryId, Long companyId);
}
