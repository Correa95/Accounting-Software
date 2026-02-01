package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.Vendor;
import com.project.backend.service.VendorService;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@RestController
@RequestMapping("/companies/{companyId}/vendors")
public class VendorController {

    private final VendorService vendorService;

    @GetMapping("/vendors")
    public ResponseEntity<List<Vendor>> getAllVendors(@PathVariable long companyId){
        return new ResponseEntity<>(vendorService.getAllVendors(companyId), HttpStatus.OK); 
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<Vendor>  getVendorById(@PathVariable long vendorId, @PathVariable long companyId) {
        return new ResponseEntity<>(vendorService.getVendorById(vendorId, companyId), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor, @PathVariable long companyId) {
        return new ResponseEntity<>(vendorService.createVendor(vendor, companyId), HttpStatus.CREATED);
    }

    @PutMapping("/{vendorId}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable long vendorId, @PathVariable long companyId, @RequestBody Vendor vendor){
        return new ResponseEntity<>(vendorService.updateVendor(vendorId, companyId, vendor), HttpStatus.OK);
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<Void> deactivateVendor(@PathVariable long vendorId, @PathVariable long companyId){
        vendorService.deactivateVendor(vendorId, companyId);
        return ResponseEntity.noContent().build();
    }
}
