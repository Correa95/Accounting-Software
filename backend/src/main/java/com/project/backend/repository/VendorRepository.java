package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Vendor;

public interface  VendorRepository extends JpaRepository<Vendor, Long>{
    List<Vendor> findByCompanyIdAndActiveTrue(long companyId);

    Optional<Vendor> findByIdAndCompanyId(long vendorId, long companyId);
}
