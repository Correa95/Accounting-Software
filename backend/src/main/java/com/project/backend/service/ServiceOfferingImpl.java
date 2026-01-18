package com.project.backend.service;

import java.util.List;

import com.project.backend.repository.ServiceOffering;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfferingImpl implements ServiceOfferingService{

    private final ServiceOffering serviceOffering;

    @Override
    public List<ServiceOffering>>  getServiceOfferings(long companyId, ServiceOffering serviceOffering){
        return serviceOffering.findById(companyId);
    }

    @Override
    public ServiceOffering getServiceOffering(long ServiceOfferingId, long companyId){
        return serviceOffering.findByIdAndCompanyId(ServiceOfferingId, companyId);
    }

    @Override
    public ServiceOffering saveServiceOffering(ServiceOffering, serviceOffering, long companyId){
        return serviceOffering.save(serviceOffering, companyId);
    }

    @Override
    public ServiceOffering updatServiceOffering(ServiceOffering serviceOffering, long companyId){
        Company company = getServiceOffering(companyId)
        
    }
    
}
