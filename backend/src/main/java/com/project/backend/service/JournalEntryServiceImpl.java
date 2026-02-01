package com.project.backend.service;

import java.time.LocalDate;
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

    @Override
    public List<JournalEntry> getAllJournalEntries(long companyId) {
        return journalEntryRepository.findByCompany_IdAndDeletedFalse(companyId);
    }

    @Override
    public JournalEntry getJournalEntryById(long journalEntryId, long companyId) {
        return journalEntryRepository.findByIdAndCompany_IdAndDeletedFalse(journalEntryId, companyId).orElseThrow(() -> new RuntimeException("Journal entry not found"));
    }

    @Transactional
    @Override
    public JournalEntry createJournalEntry(JournalEntry journalEntry,long companyId) {
        journalEntry.setStatus(JournalEntryStatus.DRAFT);
        journalEntry.setEntryDate(LocalDate.now());
        return journalEntryRepository.save(journalEntry);
    }


    @Transactional
    @Override
    public JournalEntry updateJournalEntry(long journalEntryId,long companyId,JournalEntry journalEntry) {
        JournalEntry existingJournalEntry = getJournalEntryById(journalEntryId, companyId);

        if (existingJournalEntry.getStatus() != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT entries can be updated");
        }
        existingJournalEntry.setDescription(journalEntry.getDescription());
        existingJournalEntry.setEntryDate(journalEntry.getEntryDate());
        return journalEntryRepository.save(existingJournalEntry);
    }


    @Transactional
    @Override
    public JournalEntry postJournalEntry(long journalEntryId,long companyId) {
        JournalEntry journalEntry = getJournalEntryById(journalEntryId, companyId);

        if (journalEntry.getStatus() != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT entries can be posted");
        }
        if (journalEntry.getLines().isEmpty()) {
            throw new IllegalStateException("Journal entry must have lines before posting");
        }
        journalEntry.setStatus(JournalEntryStatus.POSTED);
        journalEntry.setPostingDate(LocalDate.now());
        return journalEntryRepository.save(journalEntry);
    }

    @Transactional
    @Override
    public JournalEntry reverseJournalEntry(long journalEntryId,long companyId, String reason) {
        JournalEntry original = getJournalEntryById(journalEntryId, companyId);

        if (original.getStatus() != JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Only POSTED entries can be reversed");
        }

        JournalEntry reversal = new JournalEntry();
        reversal.setCompany(original.getCompany());
        reversal.setEntryDate(LocalDate.now());
        reversal.setDescription(
                "Reversal of JE " + original.getEntryNumber() + ": " + reason
        );
        reversal.setStatus(JournalEntryStatus.POSTED);
        reversal.setPostingDate(LocalDate.now());

        journalEntryRepository.save(reversal);

        for (JournalEntryLine line : original.getLines()) {
            JournalEntryLine reversedLine = new JournalEntryLine();
            reversedLine.setJournalEntry(reversal);
            reversedLine.setCompany(original.getCompany());
            reversedLine.setAccount(line.getAccount());
            reversedLine.setDebit(line.getCredit());
            reversedLine.setCredit(line.getDebit());
            reversedLine.setMemo("Reversal of line " + line.getId());

            journalEntryLineRepository.save(reversedLine);
        }

        original.setStatus(JournalEntryStatus.REVERSED);
        journalEntryRepository.save(original);

        return reversal;
    }

        @Transactional
    @Override
    public void deactivateJournalEntry(long journalEntryId,long companyId) {
        JournalEntry journalEntry = getJournalEntryById(journalEntryId, companyId);
        journalEntry.setDeleted(true);
        journalEntry.setDeletedAt(LocalDate.now());
        journalEntryRepository.save(journalEntry);
    }
}
