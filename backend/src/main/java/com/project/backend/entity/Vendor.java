package com.project.backend.entity;

public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private int vandorNumber;
    private String taxId;
    private String paymentTerms;
    private boolean isActive;

    
}
