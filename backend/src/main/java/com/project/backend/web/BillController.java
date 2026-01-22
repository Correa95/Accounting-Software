package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.backend.entity.Bill;
import com.project.backend.service.BillService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("companies/{companyId}/bills")
public class BillController {

    private final BillService billService;

    // Get all bills for a company
    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills(@PathVariable Long companyId) { 
        return new ResponseEntity<>(billService.getAllBills(companyId), HttpStatus.OK);
    }

    // Get a single bill
    @GetMapping("/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long companyId, @PathVariable Long billId) {
        return new ResponseEntity<>(billService.getBillById(billId, companyId), HttpStatus.OK);
    }

    // Create a bill
    @PostMapping
    public ResponseEntity<Bill> createBill(@PathVariable Long companyId, @RequestBody Bill bill) {
        return new ResponseEntity<>(billService.createBill(bill, companyId), HttpStatus.CREATED);
    }

    // Update a bill (only DRAFT allowed)
    @PutMapping("/{billId}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long companyId, @PathVariable Long billId,@RequestBody Bill bill) {
        return new ResponseEntity<>(billService.updateBill(billId, companyId, bill), HttpStatus.OK);
        
    }

    // Soft delete a bill
    @DeleteMapping("/{billId}")
    public ResponseEntity<Void> deactivateBill(@PathVariable Long companyId, @PathVariable Long billId) {
        billService.deactivateBill(billId, companyId);
        return ResponseEntity.noContent().build();
    }
}
