package com.project.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="product" uniqueConstraints = {@UniqueConstraint(columnNames = {"company_id"})})
// uniqueConstraints = {
//     @UniqueConstraint(columnNames = {"company_id", "email"})}
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private String productName;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private double unitPrice;

    @Column(nullable=false)
    private Double cost;

    @Column(nullable=false)
    private boolean isAvailable = true;

    @Column(nullable=false)
    private String invoice;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;
    
}
