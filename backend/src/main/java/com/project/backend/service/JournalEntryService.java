package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.JournalEntry;
import com.project.backend.entity.Payment;

public interface JournalEntryService {

    List<JournalEntry> getAllJournalEntries(long companyId);

    JournalEntry getJournalEntryById(long journalEntryId, long companyId);

    JournalEntry createJournalEntry(JournalEntry journalEntry, long companyId);

    JournalEntry updateJournalEntry(long journalEntryId, long companyId, JournalEntry journalEntry);

    void deactivateJournalEntry(long journalEntryId, long companyId);

    JournalEntry postJournalEntry(long journalEntryId, long companyId);

    JournalEntry reverseJournalEntry(long journalEntryId, long companyId, String reason);

    // Stripe payment integration
    JournalEntry recordStripePayment(Payment paymentOrder);
}
