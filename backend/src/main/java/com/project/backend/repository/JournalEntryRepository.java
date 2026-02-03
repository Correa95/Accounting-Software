package com.project.backend.repository;
import java.util.List;
import java.util.Optional;

import  org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.entity.JournalEntry;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByCompany_IdAndDeletedFalse(long companyId);

    Optional<JournalEntry> findByIdAndCompany_IdAndDeletedFalse(long journalEntryId, long companyId);
}
