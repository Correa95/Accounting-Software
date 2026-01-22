package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.JournalEntry;

public interface  JournalEntryService {

    List<JournalEntry> getAllJournalEntries(long companyId);

    JournalEntry getJournalEntryById(long journalEntryId, long companyId);


// createJournalEntry
// Creates DRAFT entries
// Validates lines exist
// No posting yet
    JournalEntry createJournalEntry(JournalEntry journalEntry, long companyId);


// updateJournalEntry
// Only allowed if status == DRAFT
// Re-validates debit = credit
    JournalEntry updateJournalEntry(long journalEntryId, long companyId, JournalEntry journalEntry);

    void deactivateJournalEntry(long journalEntryId, long companyId);

//  postJournalEntry
// Locks the entry
// Prevents further edits
// Makes it visible in reports
    JournalEntry postJournalEntry(long journalEntryId, long companyId);


// reverseJournalEntry
// Required by GAAP
// Creates a new entry with opposite debits/credits
// Original entry remains untouched
    // JournalEntry reverseJournalEntry(long journalEntryId, long companyId, String reason);
    
}
