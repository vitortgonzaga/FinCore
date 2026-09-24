package com.vitortgonzaga.fincore.account;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody CreateAccountRequest request){
        Account createdAccount = accountService.create(request.ownerName());
        return new AccountResponse(
                createdAccount.getId(),
                createdAccount.getOwnerName(),
                createdAccount.getStatus(),
                createdAccount.getCreatedAt());
    }

    @GetMapping("/{id}")
    public AccountResponse findById(@PathVariable UUID id){
        Account searchedAccount = accountService.findById(id);
        return new AccountResponse(
                searchedAccount.getId(),
                searchedAccount.getOwnerName(),
                searchedAccount.getStatus(),
                searchedAccount.getCreatedAt());
    }
}
