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
        return journalEntryLineRepository
                .findByJournalEntry_IdAndCompany_Id(journalEntryId, companyId);
    }

    @Transactional
    @Override
    public JournalEntryLine addJournalEntryLine(
            long journalEntryId, long companyId, JournalEntryLine line) {

        JournalEntry entry = journalEntryRepository
                .findByIdAndCompany_IdAndDeletedFalse(journalEntryId, companyId)
                .orElseThrow(() -> new RuntimeException("Journal entry not found"));

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot add lines to POSTED journal entry");
        }

        validateLine(line);

        line.setJournalEntry(entry);
        line.setCompany(entry.getCompany());

        return journalEntryLineRepository.save(line);
    }

    @Transactional
    @Override
    public void deleteJournalEntryLine(long lineId, long companyId) {
        JournalEntryLine line = journalEntryLineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("Line not found"));

        JournalEntry entry = line.getJournalEntry();

        if (!entry.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot delete lines from POSTED journal entry");
        }

        journalEntryLineRepository.delete(line);
    }

    private void validateLine(JournalEntryLine line) {
        BigDecimal debit = line.getDebit();
        BigDecimal credit = line.getCredit();

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
