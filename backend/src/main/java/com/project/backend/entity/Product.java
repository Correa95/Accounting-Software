package com.project.backend.entity;

import java.math.BigDecimal;

import com.project.backend.enums.ProductType;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames = {"company_id", "product_name"})})
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private String productName;

    @Column(nullable=false)
    private String description;

    @Column(nullable = false, unique = true)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Column(nullable = false)
    private boolean taxable;

    @Column(nullable=false, precision = 19, scale = 4)
    private BigDecimal cost;

    @Column(nullable=false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(nullable=false)
    private boolean available = true;

    @Column(nullable = false)
    private boolean trackInventory;

    private Integer quantityOnHand;
    private Integer reorderLevel;

    @Column(nullable = false)
    private boolean active = true;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;
    
}
