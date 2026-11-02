package com.vitortgonzaga.fincore.account;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account create(String ownerName){
        Account account = new Account(ownerName);
        return accountRepository.save(account);
    }

    public Account findById(UUID accountId){
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }
}
