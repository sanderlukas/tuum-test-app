package com.tuum.testassignment.account;

import jakarta.validation.Valid;

import com.tuum.testassignment.account.dto.AccountRequest;
import com.tuum.testassignment.account.dto.AccountResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping( "{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse getAccount(@PathVariable Long accountId) {
        return accountService.getAccount(accountId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@RequestBody @Valid AccountRequest request) {
        return accountService.createAccount(request);
    }
}
