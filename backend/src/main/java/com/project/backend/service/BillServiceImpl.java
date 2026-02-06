package com.project.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.backend.entity.Account;
import com.project.backend.entity.Bill;
import com.project.backend.entity.Company;
import com.project.backend.entity.Vendor;
import com.project.backend.enums.AccountSubType;
import com.project.backend.enums.BillStatus;
import com.project.backend.repository.BillRepository;
import com.project.backend.repository.CompanyRepository;
import com.project.backend.repository.VendorRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final CompanyRepository companyRepository;
    private final VendorRepository vendorRepository;
    private final AccountService accountService;

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

        Vendor vendor = vendorRepository.findById(bill.getVendor().getId())
                .filter(v -> v.getCompany().getId().equals(companyId))
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found or does not belong to company"));
        bill.setVendor(vendor);


        Account accountsPayable = accountService.getAccountBySubType(companyId, AccountSubType.ACCOUNTS_PAYABLE);
        bill.setAccount(accountsPayable);

        bill.setActive(true);
        bill.setBillStatus(BillStatus.DRAFT);

        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Long billId, Long companyId, Bill bill) {
        Bill existingBill = getBillById(billId, companyId);

        if (existingBill.getBillStatus() != BillStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT bills can be edited");
        }

        if (bill.getBillNumber() != null) existingBill.setBillNumber(bill.getBillNumber());
        if (bill.getBillDate() != null) existingBill.setBillDate(bill.getBillDate());
        if (bill.getBillDueDate() != null) existingBill.setBillDueDate(bill.getBillDueDate());
        if (bill.getBillAmount() != null) existingBill.setBillAmount(bill.getBillAmount());

        return billRepository.save(existingBill);
    }

    @Override
    public void deactivateBill(Long billId, Long companyId) {
        Bill bill = getBillById(billId, companyId);
        bill.setActive(false);
        billRepository.save(bill);
    }
}
