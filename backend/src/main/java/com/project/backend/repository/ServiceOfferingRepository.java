package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.ServiceOffering;



public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long>{
   // Return all active Service Offering for a company

    List<ServiceOffering> findByCompanyIdAndActiveTrue(long companyId);

     // Return a specific Service Offering for a company 

    Optional<ServiceOffering> findByIdAndCompanyId(long serviceOfferingId, long companyId);

}

