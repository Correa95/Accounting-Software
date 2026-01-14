package com.project.backend.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Table(name="customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String billingAddress;
    private String shippingAddress;
    private String taxId;
    private int paymentTerm;
    private double creditLimit;
    private boolean isActive;
    
}
