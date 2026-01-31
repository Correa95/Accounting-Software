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
// import com.project.backend.repository.JournalEntryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryLineServiceImpl implements JournalEntryLineService {

     private final JournalEntryLineRepository journalEntryLineRepository;
    private final JournalEntryRepository journalEntryRepository; // ✅ REQUIRED

    @Override
    public List<JournalEntryLine> getAllJournalEntryLines(long journalEntryId, long companyId) {
        return journalEntryLineRepository.findByJournalEntry_IdAndCompany_Id(journalEntryId, companyId);
    }

    @Transactional
    @Override
    public JournalEntryLine addJournalEntryLine(long journalEntryId, long companyId, JournalEntryLine journalEntryLine) {
        JournalEntry entry = getJournalEntry(journalEntryId, companyId);

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot add lines to a POSTED journal entry");
        }

        validateLine(journalEntryLine);

        journalEntryLine.setJournalEntry(entry);
        journalEntryLine.setCompany(entry.getCompany());

        return journalEntryLineRepository.save(journalEntryLine);
    }

    @Transactional
    @Override
    public JournalEntryLine updateJournalEntryLine(long journalEntryLineId, long companyId, JournalEntryLine journalEntryLine) {
        JournalEntryLine existing = journalEntryLineRepository.findById(journalEntryLineId)
                .orElseThrow(() -> throw new EntityNotFoundException("Journal entry line not found"));
                


        JournalEntry entry = existing.getJournalEntry();

        if (!entry.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot modify lines of a POSTED journal entry");
        }

        validateLine(journalEntryLine);

        existing.setDebit(journalEntryLine.getDebit());
        existing.setCredit(journalEntryLine.getCredit());
        existing.setAccount(journalEntryLine.getAccount());
        existing.setMemo(journalEntryLine.getMemo());

        return journalEntryLineRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteJournalEntryLine(long journalEntryLineId, long companyId) {
        JournalEntryLine line = journalEntryLineRepository.findById(journalEntryLineId)
                .orElseThrow(() -> new RuntimeException("Journal entry line not found"));

        JournalEntry entry = line.getJournalEntry();

        if (entry == null || entry.getCompany() == null) {
            throw new RuntimeException("Journal entry or company not found for this line");
        }

        if (!entry.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (entry.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Cannot delete lines from a POSTED journal entry");
        }

        journalEntryLineRepository.delete(line);
    }

    // ---------------- Helper Methods ----------------
    private JournalEntry getJournalEntry(long journalEntryId, long companyId) {
        return journalEntryRepository
                .findByIdAndCompany_IdAndDeletedFalse(journalEntryId, companyId)
                .orElseThrow(() -> new RuntimeException("Journal entry not found"));
    }

    private void validateLine(JournalEntryLine journalEntryLine) {
        BigDecimal debit = journalEntryLine.getDebit();
        BigDecimal credit = journalEntryLine.getCredit();

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
