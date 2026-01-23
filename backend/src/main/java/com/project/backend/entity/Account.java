package com.project.backend.entity;

import java.math.BigDecimal;

import com.project.backend.common.enums.AccountSubType;
import com.project.backend.common.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(nullable = false)
    private String accountName;

     @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;  // ASSET, LIABILITY, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountSubType accountSubType; 
    // ACCOUNTS_RECEIVABLE, ACCOUNTS_PAYABLE

    @Column(nullable = false)
    private boolean active = true;


    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;


     @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
}
