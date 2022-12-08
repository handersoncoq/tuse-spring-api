package com.tuse.tuse.services;

import com.tuse.tuse.models.Account;
import com.tuse.tuse.repositories.AccountRepo;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepo accountRepo;

    @Autowired
    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Transactional
    public Account save(Account account){
        return accountRepo.save(account);
    }

    @Transactional
    public Account getAccountByUserId(Long userId){
        return accountRepo.findAccountByUserId(userId).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public Account getAccountById(Long accountId){
        return accountRepo.findById(accountId).orElseThrow(ResourceNotFoundException::new);
    }
}
