package com.project.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.JournalEntryLine;

@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {

    List<JournalEntryLine> findByCompany_IdAndActiveTrue(long  companyId);

    List<JournalEntryLine> findByCompany_IdAndActiveTrueAndEntryDateBetween(
            long  companyId, LocalDate startDate, LocalDate endDate);

    List<JournalEntryLine> findByJournalEntry_IdAndCompany_Id(
            long  journalEntryId, long  companyId);
}
