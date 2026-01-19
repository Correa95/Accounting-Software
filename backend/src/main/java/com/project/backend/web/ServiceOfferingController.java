package com.project.backend.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.ServiceOffering;
import com.project.backend.service.ServiceOfferingService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/serviceOfferings")

public class ServiceOfferingController {
    private final  ServiceOfferingService serviceOfferingService;
    
    @GetMapping("/{serviceOfferingId}")
    public ResponseEntity<ServiceOffering> getServiceOfferings(long companyId){
        
    }
    
}
