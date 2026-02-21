package com.project.backend.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.backend.dto.TrialBalanceDTO;
import com.project.backend.service.TrialBalanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("companies/{companyId}/trial-balance")
@RequiredArgsConstructor
public class TrialBalanceController {

    private final TrialBalanceService trialBalanceService;

    @GetMapping
    public ResponseEntity<List<TrialBalanceDTO>> getTrialBalance(
            @PathVariable long companyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(trialBalanceService.getTrialBalance(companyId, startDate, endDate));
    }
}

// TrialBalance end points to test

// # All time
// GET /companies/1/trial-balance

// # Date range
// GET /companies/1/trial-balance?startDate=2025-01-01&endDate=2025-12-31