package com.project.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.project.backend.entity.Bill;
import com.project.backend.entity.Company;
import com.project.backend.repository.BillRepository;
import com.project.backend.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<Bill> getAllBills(Long companyId) {
        return billRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public Bill getBillById(Long billId, Long companyId) {
        return billRepository.findByIdAndCompanyIdAndActiveTrue(billId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found"));
    }

    @Override
    public Bill createBill(Bill bill, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        bill.setCompany(company);
        bill.setActive(true);
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Long billId, Long companyId, Bill bill) {
        Bill existing = getBillById(billId, companyId);
        if (bill.getTotalAmount() != null) existing.setTotalAmount(bill.getTotalAmount());
        if (bill.getBillDate() != null) existing.setBillDate(bill.getBillDate());
        if (bill.getBillDueDate() != null) existing.setBillDueDate(bill.getBillDueDate());
        if (bill.getVendor() != null) existing.setVendor(bill.getVendor());
        return billRepository.save(existing);
    }

    @Override
    public void deactivateBill(Long billId, Long companyId) {
        Bill bill = getBillById(billId, companyId);
        bill.setActive(false);
        billRepository.save(bill);
    }
}
