package com.project.backend.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.ServiceOffering;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/serviceOfferings")

    private final  ServiceOfferingService serviceOfferingService;

public class ServiceOfferingController {

    @GetMapping("/{serviceOfferingId}")
    public ResponseEntity<ServiceOffering> getServiceOfferings(long companyId){
        return new ResponseEntity<>(serviceOfferingService.getServiceOfferings(companyId), HttpStatus.OK);
    }
    
}
