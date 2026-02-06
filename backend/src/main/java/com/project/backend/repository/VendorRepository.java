package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Vendor;


public interface VendorRepository extends JpaRepository<Vendor, Long> {


    List<Vendor> findByCompanyIdAndActiveTrue(long companyId);

    Optional<Vendor> findByIdAndCompanyIdAndActiveTrue(long vendorId, long companyId);
     
    boolean findByCompanyIdAndVendorNumber(long companyId, Long vendorNumber);

    boolean findByCompanyIdAndEmail(long companyId, String email);

    boolean findByCompanyIdAndTaxId(long companyId, String taxId);

    Optional<Vendor> findByCompanyIdAndVendorNumberAndActiveTrue(long companyId, Long vendorNumber);
}
