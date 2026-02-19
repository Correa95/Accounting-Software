package com.project.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.entity.Account;
import com.project.backend.entity.JournalEntry;
import com.project.backend.entity.JournalEntryLine;
import com.project.backend.entity.Payment;
import com.project.backend.enums.AccountSubType;
import com.project.backend.enums.JournalEntryStatus;
import com.project.backend.repository.JournalEntryLineRepository;
import com.project.backend.repository.JournalEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryLineRepository journalEntryLineRepository;
    private final AccountService accountService;

    @Override
    public List<JournalEntry> getAllJournalEntries(long companyId) {
        return journalEntryRepository.findByCompanyIdAndDeletedFalse(companyId);
    }

    @Override
    public JournalEntry getJournalEntryById(long journalEntryId, long companyId) {
        return journalEntryRepository.findByIdAndCompanyIdAndDeletedFalse(journalEntryId, companyId)
        .orElseThrow(() -> new RuntimeException("Journal entry not found"));
    }

    @Transactional
    @Override
    public JournalEntry createJournalEntry(JournalEntry journalEntry, long companyId) {
        journalEntry.setJournalEntryStatus(JournalEntryStatus.DRAFT);
        journalEntry.setEntryDate(LocalDate.now());
        journalEntry.setEntryNumber(generateEntryNumber());
        return journalEntryRepository.save(journalEntry);
    }

    @Transactional
    @Override
    public JournalEntry updateJournalEntry(long journalEntryId, long companyId, JournalEntry journalEntry) {
        JournalEntry existing = getJournalEntryById(journalEntryId, companyId);
        if (existing.getJournalEntryStatus() != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT entries can be updated");
        }
        existing.setDescription(journalEntry.getDescription());
        existing.setEntryDate(journalEntry.getEntryDate());
        return journalEntryRepository.save(existing);
    }

    @Transactional
    @Override
    public JournalEntry postJournalEntry(long journalEntryId, long companyId) {
        JournalEntry entry = getJournalEntryById(journalEntryId, companyId);
        if (entry.getJournalEntryStatus() != JournalEntryStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT entries can be posted");
        }
        if (entry.getJournalEntryLines() == null || entry.getJournalEntryLines().isEmpty()) {
            throw new IllegalStateException("Journal entry must have at least one line");
        }
        validateBalanced(entry);
        entry.setJournalEntryStatus(JournalEntryStatus.POSTED);
        entry.setPostingDate(LocalDate.now());
        return journalEntryRepository.save(entry);
    }

    @Transactional
    @Override
    public JournalEntry reverseJournalEntry(long journalEntryId, long companyId, String reason) {
        JournalEntry journalEntry = getJournalEntryById(journalEntryId, companyId);

        if (journalEntry.getJournalEntryStatus() != JournalEntryStatus.POSTED) {
            throw new IllegalStateException("Only POSTED entries can be reversed");
        }

        JournalEntry entry = new JournalEntry();
        entry.setCompany(journalEntry.getCompany());
        entry.setEntryDate(LocalDate.now());
        entry.setEntryNumber(journalEntry.getEntryNumber() + "-R");
        entry.setDescription("Reversal of JE " + journalEntry.getEntryNumber() + ": " + reason);
        entry.setJournalEntryStatus(JournalEntryStatus.POSTED);
        entry.setPostingDate(LocalDate.now());
        entry = journalEntryRepository.save(entry);

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

        journalEntry.setJournalEntryStatus(JournalEntryStatus.REVERSED);
        journalEntryRepository.save(journalEntry);

        return entry;
    }

    @Transactional
    @Override
    public void deactivateJournalEntry(long journalEntryId, long companyId) {
        JournalEntry entry = getJournalEntryById(journalEntryId, companyId);
        entry.setDeleted(true);
        entry.setDeletedAt(LocalDateTime.now());
        journalEntryRepository.save(entry);
    }

   @Transactional
    @Override
    public JournalEntry recordPayment(Payment payment) {
    JournalEntry entry = new JournalEntry();
    entry.setCompany(payment.getInvoice().getCompany());
    entry.setEntryDate(payment.getCreatedAt().toLocalDate());
    entry.setEntryNumber("JE-" + System.currentTimeMillis());
    entry.setDescription("Stripe payment received for Invoice #" + payment.getInvoice().getId());
    entry.setJournalEntryStatus(JournalEntryStatus.POSTED);
    entry.setPostingDate(LocalDate.now());
    entry.setPayments(payment);
    entry = journalEntryRepository.save(entry);

    Account bankAccount = accountService.getOrCreateAccountBySubType(
            payment.getInvoice().getCompany().getId(),AccountSubType.BANK
    );

    Account arAccount = accountService.getOrCreateAccountBySubType(
            payment.getInvoice().getCompany().getId(),AccountSubType.ACCOUNTS_RECEIVABLE
    );

    JournalEntryLine debitLine = new JournalEntryLine();
    debitLine.setJournalEntry(entry);
    debitLine.setCompany(entry.getCompany());
    debitLine.setAccount(bankAccount);
    debitLine.setDebit(payment.getAmount());
    debitLine.setCredit(null);
    debitLine.setMemo("Payment received via Stripe");
    debitLine.setInvoice(payment.getInvoice());

    // Credit line (Accounts Receivable)
    JournalEntryLine creditLine = new JournalEntryLine();
    creditLine.setJournalEntry(entry);
    creditLine.setCompany(entry.getCompany());
    creditLine.setAccount(arAccount);
    creditLine.setDebit(null);
    creditLine.setCredit(payment.getAmount());
    creditLine.setMemo("Invoice #" + payment.getInvoice().getId());
    creditLine.setInvoice(payment.getInvoice());

    journalEntryLineRepository.save(debitLine);
    journalEntryLineRepository.save(creditLine);

    return entry;
    }


    // ----------------------------
    // INTERNAL HELPERS
    // ----------------------------
    private void validateBalanced(JournalEntry entry) {
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (JournalEntryLine line : entry.getJournalEntryLines()) {
            if (line.getDebit() != null) totalDebit = totalDebit.add(line.getDebit());
            if (line.getCredit() != null) totalCredit = totalCredit.add(line.getCredit());
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalStateException("Journal entry is not balanced");
        }
    }

    private String generateEntryNumber() {
        return "JE-" + System.currentTimeMillis();
    }
}
