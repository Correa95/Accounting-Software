package com.project.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.common.enums.JournalEntryStatus;
import com.project.backend.entity.JournalEntry;
import com.project.backend.entity.JournalEntryLine;
import com.project.backend.repository.JournalEntryLineRepository;
import com.project.backend.repository.JournalEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryLineServiceImpl implements JournalEntryLineService {

    private final JournalEntryLineRepository journalEntryLineRepository;
    private final JournalEntryRepository journalEntryRepository;

    @Override
    public List<JournalEntryLine> getJournalEntryLines(long journalEntryId, long companyId) {
        JournalEntry entry = getJournalEntry(journalEntryId, companyId);
        return entry.getLines();
    }

    @Transactional
    @Override
    public JournalEntryLine addJournalEntryLine(long journalEntryId, long companyId, JournalEntryLine line) {
        JournalEntry entry = getJournalEntry(journalEntryId, companyId);

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot add lines to a POSTED journal entry");
        }

        validateLine(line);

        line.setJournalEntry(entry);
        return journalEntryLineRepository.save(line);
    }

    @Transactional
    @Override
    public JournalEntryLine upJournalEntryLine(long lineId, long companyId, JournalEntryLine updatedLine) {
        JournalEntryLine existing = journalEntryLineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("Journal entry line not found"));

        JournalEntry entry = existing.getJournalEntry();

        if (entry.getCompany().getId() != companyId) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot modify lines of a POSTED journal entry");
        }

        validateLine(updatedLine);

        existing.setDebit(updatedLine.getDebit());
        existing.setCredit(updatedLine.getCredit());
        existing.setAccount(updatedLine.getAccount());
        existing.setMemo(updatedLine.getMemo());

        return journalEntryLineRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteJournalEntryLine(long journalEntryLineId, long companyId) {
        JournalEntryLine line = journalEntryLineRepository.findById(journalEntryLineId)
                .orElseThrow(() -> new RuntimeException("Journal entry line not found"));

        JournalEntry entry = line.getJournalEntry();

        if (entry.getCompany().getId() != companyId) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot delete lines from a POSTED journal entry");
        }

        journalEntryLineRepository.delete(line);
    }

    // ---------- HELPERS ----------
    private JournalEntry getJournalEntry(long journalEntryId, long companyId) {
        return journalEntryRepository
                .findByIdAndCompanyIdAndDeletedFalse(journalEntryId, companyId)
                .orElseThrow(() -> new RuntimeException("Journal entry not found"));
    }

    private void validateLine(JournalEntryLine line) {
        BigDecimal debit = line.getDebit();
        BigDecimal credit = line.getCredit();

        if ((debit == null || debit.signum() == 0) &&
            (credit == null || credit.signum() == 0)) {
            throw new IllegalStateException("Line must have either debit or credit");
        }

        if (debit != null && credit != null &&
            debit.signum() > 0 && credit.signum() > 0) {
            throw new IllegalStateException("Line cannot have both debit and credit");
        }
    }
}
