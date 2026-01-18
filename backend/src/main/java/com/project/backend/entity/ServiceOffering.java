package com.project.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="serviceOfferingName")
@Entity
public class ServiceOffering {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private String ServiceOfferingName;
    private String description;
    private Double hourlyRate;
    private Boolean active = true;
    @ManyToOne
    private Company company;



    
}
