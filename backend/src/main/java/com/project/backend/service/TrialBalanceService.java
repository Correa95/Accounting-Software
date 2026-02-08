package com.project.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.backend.dto.TrialBalanceDTO;
import com.project.backend.entity.JournalEntryLine;
import com.project.backend.repository.JournalEntryLineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrialBalanceService {

    private final JournalEntryLineRepository journalEntryLineRepository;

    @Transactional(readOnly = true)
    public List<TrialBalanceDTO> getTrialBalance(long companyId, LocalDate startDate, LocalDate endDate) {
        List<JournalEntryLine> journalEntryLines;

        if (startDate != null && endDate != null) {
            journalEntryLines = journalEntryLineRepository
                .findByCompanyIdAndActiveTrueAndJournalEntryEntryDateBetween(companyId, startDate, endDate);
        } else {
            journalEntryLines = journalEntryLineRepository.findByCompany_IdAndActiveTrue(companyId);
        }

        Map<String, BigDecimal> debitMap = new HashMap<>();
        Map<String, BigDecimal> creditMap = new HashMap<>();

        for (JournalEntryLine line : journalEntryLines) {
            String accountName = line.getAccount().getAccountName(); // fixed from getAccountName()

            debitMap.put(accountName, debitMap.getOrDefault(accountName, BigDecimal.ZERO)
                    .add(line.getDebit() != null ? line.getDebit() : BigDecimal.ZERO));

            creditMap.put(accountName, creditMap.getOrDefault(accountName, BigDecimal.ZERO)
                    .add(line.getCredit() != null ? line.getCredit() : BigDecimal.ZERO));
        }

        // Build DTOs
        return debitMap.keySet().stream()
                .map(account -> new TrialBalanceDTO(
                        account,
                        debitMap.getOrDefault(account, BigDecimal.ZERO),
                        creditMap.getOrDefault(account, BigDecimal.ZERO)
                ))
                .collect(Collectors.toList());
    }
}
