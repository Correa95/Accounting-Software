package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.JournalEntry;

public interface  JournalEntryLine {
    List<JournalEntryLine> getLinesByJournalEntry(long journalEntryId, long companyId);

    JournalEntryLine addJournalEntryLine(long journalEntryId, long companyId, JournalEntryLine journalEntryLine);

    JournalEntryLine updateJournalEntryLine(long journalEntryId, long companyId, JournalEntryLine journalEntryLine);

    void deleteLine(long )

}
