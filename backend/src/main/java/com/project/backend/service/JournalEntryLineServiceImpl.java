package com.project.backend.service;

import java.math.BigDecimal;
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
public class JournalEntryLineServiceImpl implements JournalEntryLineService {

    private final JournalEntryLineRepository journalEntryLineRepository;
    private final JournalEntryRepository journalEntryRepository;

    @Override
    public List<JournalEntryLine> getAllJournalEntryLines(long journalEntryId, long companyId) {
        return journalEntryLineRepository.findByJournalEntry_IdAndCompany_Id(journalEntryId, companyId);
    }

    @Transactional
    @Override
    public JournalEntryLine addJournalEntryLine(long journalEntryId, long companyId, JournalEntryLine journalEntryLine) {
        JournalEntry journalEntry = journalEntryRepository.findByIdAndCompany_IdAndDeletedFalse(journalEntryId, companyId)
                .orElseThrow(() -> new RuntimeException("Journal entry not found"));

        if (journalEntry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot add lines to a POSTED journal entry");
        }

        validateLine(journalEntryLine);

        journalEntryLine.setJournalEntry(journalEntry);
        journalEntryLine.setCompany(journalEntry.getCompany());

        return journalEntryLineRepository.save(journalEntryLine);
    }

    @Transactional
    @Override
    public JournalEntryLine updateJournalEntryLine(long journalEntryLineId, long companyId, JournalEntryLine journalEntryLine) {
        JournalEntryLine existing = journalEntryLineRepository.findById(journalEntryLineId)
                .orElseThrow(() -> new RuntimeException("Journal entry line not found"));

        JournalEntry entry = existing.getJournalEntry();

        if (!entry.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot update lines of a POSTED journal entry");
        }

        validateLine(journalEntryLine);

        existing.setAccount(journalEntryLine.getAccount());
        existing.setDebit(journalEntryLine.getDebit());
        existing.setCredit(journalEntryLine.getCredit());
        existing.setMemo(journalEntryLine.getMemo());
        existing.setInvoice(journalEntryLine.getInvoice());

        return journalEntryLineRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteJournalEntryLine(long journalEntryLineId, long companyId) {
        JournalEntryLine line = journalEntryLineRepository.findById(journalEntryLineId)
                .orElseThrow(() -> new RuntimeException("Journal entry line not found"));

        JournalEntry entry = line.getJournalEntry();

        if (!entry.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot delete lines from a POSTED journal entry");
        }

        journalEntryLineRepository.delete(line);
    }

    private void validateLine(JournalEntryLine line) {
        BigDecimal debit = line.getDebit();
        BigDecimal credit = line.getCredit();

        boolean hasDebit = debit != null && debit.signum() > 0;
        boolean hasCredit = credit != null && credit.signum() > 0;

        if (!hasDebit && !hasCredit) {
            throw new IllegalStateException("Journal entry line must have either a debit or a credit");
        }

        if (hasDebit && hasCredit) {
            throw new IllegalStateException("Journal entry line cannot have both debit and credit");
        }

        if ((debit != null && debit.signum() < 0) || (credit != null && credit.signum() < 0)) {
            throw new IllegalStateException("Debit or credit amounts cannot be negative");
        }
    }
}
