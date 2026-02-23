package com.project.backend.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.backend.entity.Account;
import com.project.backend.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("companies/{companyId}/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(@PathVariable long companyId) {
        return new ResponseEntity<>(accountService.getAllAccounts(companyId), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable long accountId) {
        return new ResponseEntity<>(accountService.getAccountById(accountId), HttpStatus.OK);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable long accountId) {
        return ResponseEntity.ok(accountService.getAccountBalance(accountId));
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @PathVariable long companyId,
            @RequestBody Account account) {
        return new ResponseEntity<>(accountService.createAccount(companyId, account), HttpStatus.CREATED);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable long accountId,
            @RequestBody Account account) {
        return new ResponseEntity<>(accountService.updateAccount(accountId, account), HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deactivateAccount(@PathVariable long accountId) {
        accountService.deactivateAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}

// GET    http://localhost:8080/companies/1/accounts
// GET    http://localhost:8080/companies/1/accounts/1
// GET    http://localhost:8080/companies/1/accounts/1/balance   ← computed from journal entries
// POST   http://localhost:8080/companies/1/accounts
// PUT    http://localhost:8080/companies/1/accounts/1
// DELETE http://localhost:8080/companies/1/accounts/1