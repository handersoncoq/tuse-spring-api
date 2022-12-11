package com.tuse.tuse.controllers;

import com.tuse.tuse.models.Account;
import com.tuse.tuse.models.User;
import com.tuse.tuse.services.AccountService;
import com.tuse.tuse.services.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class AccountController {

    private final AccountService acctService;
    private final UserService userService;

    public AccountController(AccountService acctService, UserService userService) {
        this.acctService = acctService;
        this.userService = userService;
    }

    @GetMapping
    public Account getUserAccount(){
        User user = userService.getSessionUser();
        return acctService.getUserAccount(user);
    }

    @GetMapping("/balance")
    public Double getUserAccountBalance(){
        User user = userService.getSessionUser();
        Account userAccount = acctService.getUserAccount(user);
        return userAccount.getBalance();
    }
}
