package com.project.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrialBalanceDTO {

    private String accountName;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    public BigDecimal getBalance() {
        return totalDebit.subtract(totalCredit);
    }
}