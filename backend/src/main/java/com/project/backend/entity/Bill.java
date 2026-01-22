package com.project.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.project.backend.common.enums.BillStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bills")
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private String billNumber;

    @Column(nullable = false)
    private LocalDate billDate;

    @Column(nullable = false)
    private LocalDate billDueDate;

    @Column(nullable = false)
    private BillStatus billStatus = BillStatus.DRAFT;

    @Column(updatable = false)
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;


    @Column(nullable = false, precision = 19, scale= 4)
    private BigDecimal totalAmount;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;


    
}
