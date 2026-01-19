package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.ServiceOffering;
import com.project.backend.service.ServiceOfferingService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/serviceOfferings")

public class ServiceOfferingController {
    private final  ServiceOfferingService serviceOfferingService;
    
    // Get all customers for a company
    @GetMapping
    public ResponseEntity<List<ServiceOffering>> getServiceOfferings(@PathVariable long companyId){
        return new ResponseEntity<>(serviceOfferingService.getServiceOfferings(companyId), HttpStatus.OK);
    }

    // Get a single customer
    @GetMapping("/{serviceOfferingId}")
    public ResponseEntity<ServiceOffering> getServiceOffering(@PathVariable long serviceOfferingId, @PathVariable long companyId) {
        return new ResponseEntity<>(serviceOfferingService.getServiceOffering(serviceOfferingId, companyId), HttpStatus.OK);
    }

    // Creating Service Offering
    @PostMapping
    public ResponseEntity<ServiceOffering> createServiceOffering(@RequestBody ServiceOffering serviceOffering, @PathVariable long companyId) {
        return new ResponseEntity<>(serviceOfferingService.createServiceOffering(serviceOffering, companyId), HttpStatus.CREATED);
    }

    // Updating Service Offering
    @PutMapping("/{serviceOfferingId}")
    public ResponseEntity<ServiceOffering> updateServiceOffering(@PathVariable long serviceOfferingId, @PathVariable long companyId, @RequestBody ServiceOffering serviceOffering) {
        return new ResponseEntity<>(serviceOfferingService.updateServiceOffering(serviceOfferingId, companyId, serviceOffering), HttpStatus.OK);
    }

    // Deleting Service Offering
    @DeleteMapping("/{serviceOfferingId}")
    public ResponseEntity<void> deleteServiceOffering(@PathVariable long serviceOfferingId, @PathVariable long companyId){
        servi
        return new ResponseEntity.noContent().build();
    }
    


    
    
}
