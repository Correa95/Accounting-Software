package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.JournalEntry;

public interface JournalEntryService {

    List<JournalEntry> getAllJournalEntries(long companyId);

    JournalEntry getJournalEntryById(long journalEntryId, long companyId);

    // Creates DRAFT journal entry
    JournalEntry createJournalEntry(JournalEntry journalEntry, long companyId);

    // Only allowed if status == DRAFT
    JournalEntry updateJournalEntry(long journalEntryId,long companyId,JournalEntry journalEntry);

    // Soft delete
    void deactivateJournalEntry(long journalEntryId, long companyId);

    // Locks entry and posts it
    JournalEntry postJournalEntry(long journalEntryId, long companyId);

    // GAAP-compliant reversal
    JournalEntry reverseJournalEntry(long journalEntryId,long companyId, String reason
    );
}
