package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.JournalEntryLine;

public interface JournalEntryLineService {

    List<JournalEntryLine> getAllJournalEntryLines(
            long journalEntryId,
            long companyId
    );

    JournalEntryLine addJournalEntryLine(
            long journalEntryId,
            long companyId,
            JournalEntryLine journalEntryLine
    );

    JournalEntryLine updateJournalEntryLine(
            long journalEntryLineId,
            long companyId,
            JournalEntryLine journalEntryLine
    );

    void deleteJournalEntryLine(
            long journalEntryLineId,
            long companyId
    );
}
