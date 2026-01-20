package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Vendor;

public interface  VendorService {
    List<Vendor> getAllVendors(long companyId);
    Vendor getVendorById(long vendorId, long companyId);
    Vendor createVendor(Vendor vendor, long companyId);
    Vendor updateVendor(long vendorId, long companyId, Vendor vendor);
    void  deactivateVendor(long vendorId, long companyId);
    
}
