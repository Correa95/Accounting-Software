package com.project.backend.web;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.BankAccount;
import com.project.backend.service.BankAccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/companies/{companyId}/bankAccounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping  
    public List<BankAccount> getBankAccounts(@PathVariable long companyId) {
        return bankAccountService.getBankAccountsByCompany(companyId);
    }

    @PostMapping
    public BankAccount createBankAccount(@PathVariable long companyId, @RequestBody BankAccount account) {
        account.setCompany(new Company(companyId)); // set only id
        return bankAccountService.createBankAccount(account);
    }

    @PutMapping("/{accountId}")
    public BankAccount updateBankAccount(@PathVariable long accountId, @RequestBody BankAccount account) {
        return bankAccountService.updateBankAccount(accountId, account);
    }
    
    @DeleteMapping("/{accountId}")
    public void deactivateBankAccount(@PathVariable long accountId) {
        bankAccountService.deactivateBankAccount(accountId);
    }
}

