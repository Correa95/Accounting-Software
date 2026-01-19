package com.project.backend.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.ServiceOffering;

@RestController
@RequestMapping("companies/{companyId}/serviceOffering")
public class ServiceOfferingController {

    @GetMapping("/{serviceOfferingId}")
    public ResponseEntity<ServiceOffering> getServiceOfferings(long companyId){
        return new ResponseEntity<>(serviceOfferingRepository.getServiceOfferings(companyId), HttpStatus.OK);
    }
    
}
