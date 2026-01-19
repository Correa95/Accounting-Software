package com.project.backend.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.ServiceOffering;
import com.project.backend.service.ServiceOfferingService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@AllArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/serviceOfferings")

public class ServiceOfferingController {
    private final  ServiceOfferingService serviceOfferingService;
    
    @GetMapping()
    public ResponseEntity<ServiceOffering> getServiceOfferings(@PathVariable long companyId){
        return  new ResponseEntity<>(serviceOfferingService.getServiceOfferings(companyId), HttpStatus.OK);
    }

    @GetMapping("/{serviceOfferingId}")
    public ResponseEntity<ServiceOffering> getServiceOffering(@PathVariable long serviceOfferingId, @PathVariable long companyId) {
        return new ResponseEntity<>(serviceOfferingService.getServiceOffering(serviceOfferingId, companyId));
    }
    
    
}
