package com.project.backend.service;

import java.util.List;

import com.project.backend.repository.VendorRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Company;
import com.project.backend.entity.Vendor;
import com.project.backend.repository.CompanyRepository;

@Service
public class VendorServiceImpl implements VendorService{

    private final VendorRepository vendorRepository;
    private final CompanyRepository companyRepository;

    
    @Override
    public List<Vendor> getVendors(long companyId){
        return vendorRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public Vendor getVendor(long vendorId, long companyId){
        return vendorRepository.findByIdAndCompanyId(vendorId, companyId).orElseThrow(() -> new EntityNotFoundException("Vendor not found"));
    }

    @Override
    public  Vendor createVendor(Vendor vendor, long companyId){

        Company company = companyRepository.findById(companyId);
        vendor.setCompany(company);
        vendor.setActive(true);
        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor updateVendor(long vendorId, long companyId, Vendor vendor){
        Vendor existingVendor = getVendor(vendorId, companyId);

        if(vendor.getVendorName() != null){existingVendor.setVendorName(vendor.getVendorName());}
        if(vendor.getEmail() != null){existingVendor.setEmail(vendor.getEmail());}
        if(vendor.getAddress() != null){ existingVendor.setAddress(vendor.getAddress());}
        if(vendor.getPhone() != null){ existingVendor.setPhone(vendor.getPhone());}
        if(vendor.getTaxId() != null){ existingVendor.setTaxId(vendor.getTaxId());}
        if(vendor.getPaymentTerms() != null){ existingVendor.setPaymentTerms(vendor.getPaymentTerms());}
        if(vendor.getVendorNumber() != null){ existingVendor.setVendorNumber(vendor.getVendorNumber());}

        return vendorRepository.save(existingVendor);
    }

    @Override
    public void deleteVendor(long vendorId, long companyId){
        Vendor vendor = getVendor(vendorId, companyId);
        vendor.setActive(false);
        vendorRepository.save(vendor);
    }
}
