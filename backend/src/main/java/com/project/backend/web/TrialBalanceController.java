package com.project.backend.web;


import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.backend.dto.TrialBalanceDTO;
import com.project.backend.service.TrialBalanceService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("companies/{companyId}/trial-balance")
public class TrialBalanceController {

    private final TrialBalanceService trialBalanceService;

    @GetMapping
    public ResponseEntity<List<TrialBalanceDTO>> getTrialBalance(
            @PathVariable Long companyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return new ResponseEntity<>(trialBalanceService.getTrialBalance(companyId, startDate, endDate), HttpStatus.OK); 
    }
}
