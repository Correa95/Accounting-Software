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
        JournalEntry journalEntry = journalEntryRepository.findByIdAndCompany_IdAndDeletedFalse(journalEntryId, companyId).orElseThrow(() -> new RuntimeException("Journal entry not found"));
        if (journalEntry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot add lines to POSTED journal entry");
        }
        validateLine(journalEntryLine);
        journalEntryLine.setJournalEntry(journalEntry);
        journalEntryLine.setCompany(journalEntry.getCompany());
        return journalEntryLineRepository.save(journalEntryLine);
    }

    @Transactional
    @Override
    public JournalEntryLine updateJournalEntryLine(long journalEntryLineId, long companyId,JournalEntryLine journalEntryLine) {
        JournalEntryLine existingJournalEntryLine = journalEntryLineRepository.findById(journalEntryLineId).orElseThrow(() -> new RuntimeException("Journal entry line not found"));
        JournalEntry entry = existingJournalEntryLine.getJournalEntry();

        if (!entry.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot update lines of POSTED journal entry");
        }

        validateLine(journalEntryLine);

        existingJournalEntryLine.setAccount(journalEntryLine.getAccount());
        existingJournalEntryLine.setDebit(journalEntryLine.getDebit());
        existingJournalEntryLine.setCredit(journalEntryLine.getCredit());
        existingJournalEntryLine.setMemo(journalEntryLine.getMemo());

        return journalEntryLineRepository.save(existingJournalEntryLine);
    }

    @Transactional
    @Override
    public void deleteJournalEntryLine(
            long journalEntryLineId,
            long companyId
    ) {
        JournalEntryLine journalEntryLine = journalEntryLineRepository
                .findById(journalEntryLineId)
                .orElseThrow(() -> new RuntimeException("Line not found"));

        JournalEntry entry = journalEntryLine.getJournalEntry();

        if (!entry.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot delete lines from POSTED journal entry");
        }

        journalEntryLineRepository.delete(journalEntryLine);
    }

    private void validateLine(JournalEntryLine journalEntryLine) {
        BigDecimal debit = journalEntryLine.getDebit();
        BigDecimal credit = journalEntryLine.getCredit();

        if ((debit == null || debit.signum() == 0)
                && (credit == null || credit.signum() == 0)) {
            throw new IllegalStateException("Line must have debit or credit");
        }

        if (debit != null && credit != null
                && debit.signum() > 0 && credit.signum() > 0) {
            throw new IllegalStateException("Line cannot have both debit and credit");
        }
    }
}
