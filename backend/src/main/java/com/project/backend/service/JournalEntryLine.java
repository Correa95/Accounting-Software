package com.project.backend.service;

public interface  JournalEntryLine {
    List<JournalEntryLine> getLinesByJournalEntry(long journalEntryId, long companyId);
}
