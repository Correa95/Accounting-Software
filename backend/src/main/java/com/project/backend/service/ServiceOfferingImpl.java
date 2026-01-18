package com.project.backend.service;

import java.util.List;

import com.project.backend.repository.ServiceOffering;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfferingImpl implements ServiceOfferingService{

    private final ServiceOffering serviceOffering;

    public List<ServiceOffering>ServiceOfferingImpl> getServiceOffering(long companyId, ServiceOffering serviceOffering){
        return serviceOffering.findById(companyId)
        
    }
    
}
