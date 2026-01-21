package com.project.backend.service;

import java.util.List;

import com.project.backend.entity.Bill;

public interface BillService {
    List<Bill> getAllBills(Long companyId);
    Bill getBillById(Long billId, Long companyId);
    Bill createBill(Bill bill, Long companyId);
    Bill updateBill(Long billId, Long companyId, Bill bill);
    void deactivateBill(Long billId, Long companyId);
}
