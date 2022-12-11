package com.tuse.tuse.controllers;

import com.tuse.tuse.models.User;
import com.tuse.tuse.requests.PurchaseRequest;
import com.tuse.tuse.responses.PurchaseResponse;
import com.tuse.tuse.services.PurchaseService;
import com.tuse.tuse.services.UserService;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchase")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserService userService;

    public PurchaseController(PurchaseService purchaseService, UserService userService) {

        this.purchaseService = purchaseService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public String execute(@RequestBody PurchaseRequest purchaseRequest) {
        try {
            User user = userService.getSessionUser();
            if(user == null){
               User nonSubscriber = new User();
                nonSubscriber.setUsername("Tu$eUser" + UUID.randomUUID().getLeastSignificantBits());
                nonSubscriber.setPassword(String.valueOf(UUID.randomUUID().getLeastSignificantBits()));
                userService.save(nonSubscriber);
                purchaseService.save(purchaseRequest, nonSubscriber);
                return "Your purchase of the stock '"+ purchaseRequest.getSymbol()+"' was successful" ;
            }
            purchaseService.execute(purchaseRequest, user);
            return "Your purchase of the stock '"+ purchaseRequest.getSymbol()+"' was successful";
        } catch (InvalidUserInputException | ResourcePersistenceException | UnauthorizedException |
                 ResourceNotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping()
    public List<PurchaseResponse> getPurchasesByUser(){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        return purchaseService.findPurchasesByUserId(user.getUserId());
    }

    @GetMapping("/all")
    public List<PurchaseResponse> getAllPurchases(){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return purchaseService.findAllPurchases();
    }

    @GetMapping("/symbol/{symbol}")
    public List<PurchaseResponse> GetPurchasesBySymbol(@PathVariable String symbol){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return purchaseService.getPurchasesBySymbol(symbol.trim());
    }

    @GetMapping("/company/{company}")
    public List<PurchaseResponse> GetPurchasesByCompany(@PathVariable String company){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return purchaseService.getPurchasesByCompany(company.trim());
    }
}

