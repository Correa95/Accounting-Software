package com.project.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.JournalEntry;
import com.project.backend.entity.JournalEntryLine;
import com.project.backend.enums.JournalEntryStatus;
import com.project.backend.repository.JournalEntryLineRepository;
import com.project.backend.repository.JournalEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryLineRepository journalEntryLineRepository;

    // ----------------------------
    // READ
    // ----------------------------

    @Override
    public List<JournalEntry> getAllJournalEntries(long companyId) {
        return journalEntryRepository.findByCompany_IdAndDeletedFalse(companyId);
    }

    @Override
    public JournalEntry getJournalEntryById(long journalEntryId, long companyId) {
        return journalEntryRepository
                .findByIdAndCompany_IdAndDeletedFalse(journalEntryId, companyId)
                .orElseThrow(() -> new RuntimeException("Journal entry not found"));
    }

    // ----------------------------
    // CREATE (DRAFT)
    // ----------------------------

    @Transactional
    @Override
    public JournalEntry createJournalEntry(JournalEntry journalEntry, long companyId) {
        journalEntry.setStatus(JournalEntryStatus.DRAFT);
        journalEntry.setEntryDate(LocalDate.now());
        journalEntry.setEntryNumber(generateEntryNumber());
        // ⚠️ company must already be set (controller or Stripe service)
        return journalEntryRepository.save(journalEntry);
    }

    // ----------------------------
    // UPDATE (DRAFT ONLY)
    // ----------------------------

    @Transactional
    @Override
    public JournalEntry updateJournalEntry(
            long journalEntryId,
            long companyId,
            JournalEntry journalEntry
    ) {
        JournalEntry existing = getJournalEntryById(journalEntryId, companyId);

        if (existing.getStatus() != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT entries can be updated");
        }

        existing.setDescription(journalEntry.getDescription());
        existing.setEntryDate(journalEntry.getEntryDate());

        return journalEntryRepository.save(existing);
    }

    // ----------------------------
    // POST (LOCK ENTRY)
    // ----------------------------

    @Transactional
    @Override
    public JournalEntry postJournalEntry(long journalEntryId, long companyId) {
        JournalEntry entry = getJournalEntryById(journalEntryId, companyId);

        if (entry.getStatus() != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT entries can be posted");
        }

        if (entry.getJournalEntryLines() == null || entry.getJournalEntryLines().isEmpty()) {
            throw new IllegalStateException("Journal entry must have at least one line");
        }

        validateBalanced(entry);

        entry.setStatus(JournalEntryStatus.POSTED);
        entry.setPostingDate(LocalDate.now());

        return journalEntryRepository.save(entry);
    }

    // ----------------------------
    // REVERSE (GAAP COMPLIANT)
    // ----------------------------

    @Transactional
    @Override
    public JournalEntry reverseJournalEntry(long journalEntryId,long companyId, String reason) {
        JournalEntry journalEntry = getJournalEntryById(journalEntryId, companyId);

        if (journalEntry.getStatus() != JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Only POSTED entries can be reversed");
        }

        JournalEntry entry = new JournalEntry();
        entry.setCompany(journalEntry.getCompany());
        entry.setEntryDate(LocalDate.now());
        entry.setEntryNumber(journalEntry.getEntryNumber() + "-R");
        entry.setDescription("Reversal of JE " + journalEntry.getEntryNumber() + ": " + reason);
        entry.setStatus(JournalEntryStatus.POSTED);
        entry.setPostingDate(LocalDate.now());

        journalEntryRepository.save(entry);

        for (JournalEntryLine line : journalEntry.getJournalEntryLines()) {
            JournalEntryLine reversedLine = new JournalEntryLine();
            reversedLine.setJournalEntry(entry);
            reversedLine.setCompany(journalEntry.getCompany());
            reversedLine.setAccount(line.getAccount());
            reversedLine.setDebit(line.getCredit());
            reversedLine.setCredit(line.getDebit());
            reversedLine.setMemo("Reversal of line " + line.getId());

            journalEntryLineRepository.save(reversedLine);
        }

        journalEntry.setStatus(JournalEntryStatus.REVERSED);
        journalEntryRepository.save(journalEntry);

        return entry;
    }

    // ----------------------------
    // SOFT DELETE
    // ----------------------------

    @Transactional
    @Override
    public void deactivateJournalEntry(long journalEntryId, long companyId) {
        JournalEntry entry = getJournalEntryById(journalEntryId, companyId);
        entry.setDeleted(true);
        entry.setDeletedAt(LocalDateTime.now());
        journalEntryRepository.save(entry);
    }

    // ----------------------------
    // VALIDATION
    // ----------------------------

    private void validateBalanced(JournalEntry entry) {
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (JournalEntryLine line : entry.getJournalEntryLines()) {
            if (line.getDebit() != null) {
                totalDebit = totalDebit.add(line.getDebit());
            }
            if (line.getCredit() != null) {
                totalCredit = totalCredit.add(line.getCredit());
            }
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalStateException("Journal entry is not balanced");
        }
    }

    // ----------------------------
    // INTERNAL HELPERS
    // ----------------------------

    private String generateEntryNumber() {
        return "JE-" + System.currentTimeMillis();
    }
}
