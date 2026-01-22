package com.project.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.common.enums.JournalEntryStatus;
import com.project.backend.entity.JournalEntry;
import com.project.backend.repository.JournalEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryServiceImpl implements JournalEntryService{
    
    private final JournalEntryRepository journalEntryRepository;

    @Override
    public List<JournalEntry> getAllJournalEntries(long companyId){
        return journalEntryRepository.findByCompanyIdAndDeletedFalse(companyId);
    }

    @Override 
    public JournalEntry getJournalEntryById(long journalEntryId, long companyId){
        return journalEntryRepository.findByIdAndCompanyIdAndDeletedFalse(journalEntryId, companyId).orElseThrow(()-> new RuntimeException("Journal entry not found"));
    }

    @Override
    public JournalEntry createJournalEntry(JournalEntry journalEntry, long companyId){
        journalEntry.setStatus(JournalEntryStatus.DRAFT);
        journalEntry.setDeleted(false);
        return journalEntryRepository.save(journalEntry);
    }

    @Override
    public JournalEntry updateJournalEntry(
        long journalEntryId, 
        long companyId, 
        JournalEntry journalEntry){
        JournalEntry existingJournal = getJournalEntryById(journalEntryId, companyId);
        if (existingJournal.getStatus() == JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Posted journal entries cannot be modified");
        }

        existingJournal.setEntryDate(journalEntry.getEntryDate());
        existingJournal.setDescription(journalEntry.getDescription());
        existingJournal.setLines(journalEntry.getLines());

        return journalEntryRepository.save(existingJournal);
    }

    @Override
    public void deactivateJournalEntry(long journalEntryId, long companyId){

        JournalEntry journalEntry = getJournalEntryById(journalEntryId, companyId);
        journalEntry.setDeleted(true);
        journalEntry.setDeletedAt(LocalDate.now());
        journalEntryRepository.save(journalEntry);
    }

    // Optional: post a journal entry (locks it)
    @Transactional
    @Override
    public JournalEntry postJournalEntry(long  journalEntryId, long  companyId) {
        JournalEntry journalEntry = getJournalEntryById(journalEntryId, companyId);
        if (journalEntry.getStatus() != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT journal entries can be posted");
        }
        // 🔐 VALIDATION
        validateBalanced(journalEntry);
        // add debit = credit validation before posting
        journalEntry.setStatus(JournalEntryStatus.POSTED);
        journalEntry.setPostingDate(LocalDate.now());
        journalEntry.setPostedBy("system"); // replace with logged-in user later

        return journalEntryRepository.save(journalEntry);
    }

    // Optional: reverse a journal entry (creates new entry with opposite amounts)
    @Transactional
    @Override
    public JournalEntry reverseJournalEntry(long journalEntryId, long companyId, String reason) {
        JournalEntry original = getJournalEntryById(journalEntryId, companyId);
        // implement reversing logic: flip debit/credit amounts
        JournalEntry reversal = new JournalEntry();
        reversal.setEntryDate(LocalDate.now());
        reversal.setDescription("Reversal of JE " + original.getEntryNumber() + ": " + reason);
        reversal.setStatus(JournalEntryStatus.DRAFT);
        reversal.setCompany(original.getCompany());
        reversal.setLines(original.getLines()); // later flip debits/credits
        return journalEntryRepository.save(reversal);
    }
    

    private void validateBalanced(JournalEntry journalEntry) {
    BigDecimal totalDebit = journalEntry.getLines().stream()
            .map(line -> line.getDebit() == null ? BigDecimal.ZERO : line.getDebit())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCredit = journalEntry.getLines().stream()
            .map(line -> line.getCredit() == null ? BigDecimal.ZERO : line.getCredit())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (totalDebit.compareTo(totalCredit) != 0) {
        throw new IllegalStateException(
            "Journal entry is not balanced. Debit: " + totalDebit + ", Credit: " + totalCredit
        );
    }
}

}
