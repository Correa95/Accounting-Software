package com.project.backend.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TrialBalanceDTO {
    private String accountName;
    private BigDecimal debitTotal;
    private BigDecimal creditTotal;
}
