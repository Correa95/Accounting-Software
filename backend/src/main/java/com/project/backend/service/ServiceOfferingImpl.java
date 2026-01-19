package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Company;
import com.project.backend.entity.ServiceOffering;
import com.project.backend.repository.ServiceOfferingRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceOfferingImpl implements ServiceOfferingService{

    private final ServiceOfferingRepository serviceOfferingRepository;
    private final  CompanyService companyService;

    @Override
    public List<ServiceOffering>  getServiceOfferings(long companyId){
        return serviceOfferingRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public ServiceOffering getServiceOffering(long serviceOfferingId, long companyId){
        return serviceOfferingRepository.findByIdAndCompanyId(serviceOfferingId, companyId).orElseThrow(() -> new EntityNotFoundException("Service Offering not found"));
    }

    @Override
    public ServiceOffering createServiceOffering(ServiceOffering serviceOffering, long companyId){
        Company company = companyService.getCompany(companyId);

        serviceOffering.setCompany(company);
        serviceOffering.setActive(true);

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateServiceOffering(long serviceOfferingId, ServiceOffering updatedServiceOffering, long companyId){

        ServiceOffering existing = getServiceOffering(serviceOfferingId, companyId);

        existing.setServiceOfferingName(updatedServiceOffering.getServiceOfferingName());
        existing.setDescription(updatedServiceOffering.getDescription());
        existing.setHourlyRate(updatedServiceOffering.getHourlyRate());
        existing.setActive(updatedServiceOffering.getActive());

        return serviceOfferingRepository.save(existing);
       
    }

    @Override
    public void deleteServiceOffering(long serviceOfferingId, long companyId){
        ServiceOffering serviceOffering = getServiceOffering(serviceOfferingId, companyId);
        serviceOffering.setActive(false);
        serviceOfferingRepository.save(serviceOffering);
    }
    
}
