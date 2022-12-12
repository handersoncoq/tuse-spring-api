package com.tuse.tuse.services;

import com.tuse.tuse.models.Account;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.AccountRepo;
import com.tuse.tuse.requests.PurchaseRequest;
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
    public void save(Account account){
        accountRepo.save(account);
    }

    @Transactional
    public void updatePurchaseUserAccount(User purchaseUser, PurchaseRequest purchaseRequest){
        Account userAccount = getUserAccount(purchaseUser);
        Double amount = purchaseRequest.getQuantity() * purchaseRequest.getBuyingPrice();
        userAccount.setBalance(userAccount.getBalance() - amount);
        save(userAccount);
    }

    @Transactional
    public void updateSaleUserAccount(User saleUser, PurchaseRequest purchaseRequest){
        Account userAccount = getUserAccount(saleUser);
        Double amount = purchaseRequest.getQuantity() * purchaseRequest.getBuyingPrice();
        userAccount.setBalance(userAccount.getBalance() + amount);
        save(userAccount);
    }

    @Transactional
    public Account getUserAccount(User user){
        if(user == null) throw new ResourceNotFoundException("No user was found");
        return accountRepo.findAccountByUserId(user.getUserId()).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public Account getAccountById(Long accountId){
        return accountRepo.findById(accountId).orElseThrow(ResourceNotFoundException::new);
    }
}
