package com.project.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.JournalEntry;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long>{
    List<JournalEntry> findByCompanyIdAndDeletedFalse(long companyId);
    Optional<JournalEntry> findByIdAndCompanyIdAndDeletedFalse(long journalEntryId, long companyId);

     List<JournalEntry> findByCompanyIdAndEntryDateBetweenAndDeletedFalse(
        Long companyId,
        LocalDate startDate,
        LocalDate endDate
    );
    
} 
    
