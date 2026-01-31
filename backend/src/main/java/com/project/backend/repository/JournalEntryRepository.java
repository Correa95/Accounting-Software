package com.project.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.JournalEntryLine;

@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {

    // All active lines for a company
    List<JournalEntryLine> findByCompany_IdAndActiveTrue(Long companyId);

    // Active lines for company filtered by date range
    List<JournalEntryLine> findByCompany_IdAndActiveTrueAndEntryDateBetween(
            Long companyId, LocalDate startDate, LocalDate endDate);

    // Fix for your red line: get all lines for a journal entry and company
    List<JournalEntryLine> findByJournalEntryIdAndCompany_Id(Long journalEntryId, Long companyId);
}
