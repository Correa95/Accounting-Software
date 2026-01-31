package com.project.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.JournalEntry;
import com.project.backend.enums.JournalEntryStatus;
import com.project.backend.repository.JournalEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

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

    @Override
    public JournalEntry createJournalEntry(JournalEntry journalEntry, long companyId) {
        journalEntry.setStatus(JournalEntryStatus.DRAFT);
        journalEntry.setDeleted(false);
        return journalEntryRepository.save(journalEntry);
    }

    @Override
    public JournalEntry updateJournalEntry(
            long journalEntryId,
            long companyId,
            JournalEntry journalEntry) {

        JournalEntry existing = getJournalEntryById(journalEntryId, companyId);

        if (existing.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Posted journal entries cannot be modified");
        }

        existing.setEntryDate(journalEntry.getEntryDate());
        existing.setDescription(journalEntry.getDescription());

        existing.getLines().clear();
        journalEntry.getLines().forEach(line -> {
            line.setJournalEntry(existing);
            existing.getLines().add(line);
        });

        return journalEntryRepository.save(existing);
    }

    @Override
    public void deactivateJournalEntry(long journalEntryId, long companyId) {
        JournalEntry journalEntry = getJournalEntryById(journalEntryId, companyId);
        journalEntry.setDeleted(true);
        journalEntry.setDeletedAt(LocalDate.now());
        journalEntryRepository.save(journalEntry);
    }

    @Transactional
    @Override
    public JournalEntry postJournalEntry(long journalEntryId, long companyId) {
        JournalEntry journalEntry = getJournalEntryById(journalEntryId, companyId);

        if (journalEntry.getStatus() != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT journal entries can be posted");
        }

        validateBalanced(journalEntry);

        journalEntry.setStatus(JournalEntryStatus.POSTED);
        journalEntry.setPostingDate(LocalDate.now());
        journalEntry.setPostedBy("system");

        return journalEntryRepository.save(journalEntry);
    }

    private void validateBalanced(JournalEntry journalEntry) {
        BigDecimal totalDebit = journalEntry.getLines().stream()
                .map(l -> l.getDebit() == null ? BigDecimal.ZERO : l.getDebit())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = journalEntry.getLines().stream()
                .map(l -> l.getCredit() == null ? BigDecimal.ZERO : l.getCredit())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalStateException(
                "Journal entry not balanced. Debit: " + totalDebit +
                ", Credit: " + totalCredit
            );
        }
    }
}
