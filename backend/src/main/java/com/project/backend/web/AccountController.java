package com.project.backend.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.Account;
import com.project.backend.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/companies/{companyId}/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts(@PathVariable long companyId) {
        return accountService.getAllAccounts(companyId);
    }

    @GetMapping("/subType/{subType}")
    public Account getAccountBySubType(@PathVariable long companyId,
                                       @PathVariable String subType) {
        return accountService.getAccountBySubType(companyId, Enum.valueOf(com.project.backend.enums.AccountSubType.class, subType));
    }

    @PostMapping
    public Account createAccount(@PathVariable long companyId, @RequestBody Account account) {
        return accountService.createAccount(companyId, account);
    }

    @PutMapping("/{accountId}")
    public Account updateAccount(@PathVariable long accountId, @RequestBody Account account) {
        return accountService.updateAccount(accountId, account);
    }

    @DeleteMapping("/{accountId}")
    public void deactivateAccount(@PathVariable long accountId) {
        accountService.deactivateAccount(accountId);
    }
}
