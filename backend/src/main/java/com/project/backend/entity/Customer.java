package com.project.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Table(name="customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private String phone;

    @Column(nullable=false)
    private String billingAddress;
    
    @Column(nullable=false)
    private String shippingAddress;

    @Column(nullable=false)
    private String taxId;

    @Column(nullable=false)
    private int paymentTerm;

    @Column(nullable=false)
    private double creditLimit;

    @Column(nullable=false)
    private boolean isActive;

    
}
