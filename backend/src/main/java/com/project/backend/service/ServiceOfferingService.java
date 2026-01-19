package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.ServiceOffering;

public interface ServiceOfferingService {
    List<ServiceOffering> getServiceOfferings(long companyId);

    ServiceOffering getServiceOffering(long serviceOfferingId, long companyId);

    ServiceOffering createServiceOffering(ServiceOffering serviceOffering, long companyId);

    // ServiceOffering updateServiceOffering(ServiceOffering serviceOffering, long companyId);
    
    ServiceOffering updateServiceOffering(long serviceOfferingId, ServiceOffering updatedServiceOffering, long companyId);
    void deleteServiceOffering(long serviceOfferingId, long companyId);
}




