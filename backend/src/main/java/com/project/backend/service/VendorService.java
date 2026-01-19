package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Vendor;




public interface  VendorService {
    List<Vendor> getVendors(long companyId);
    Vendor getVendor(long vendorId, long companyId);
    Vendor createVendor(Vendor vendor, long companyId);
    
}
