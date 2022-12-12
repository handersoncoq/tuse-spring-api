package com.tuse.tuse.controllers;

import com.tuse.tuse.models.User;
import com.tuse.tuse.requests.SaleRequest;
import com.tuse.tuse.responses.SaleResponse;
import com.tuse.tuse.services.SaleService;
import com.tuse.tuse.services.UserService;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class SaleController {

    private final SaleService saleService;
    private final UserService userService;

    public SaleController(SaleService saleService, UserService userService) {

        this.saleService = saleService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public String execute(@RequestBody SaleRequest saleRequest) {
        try {
            User user = userService.getSessionUser();
            saleService.execute(saleRequest, user);
            return "Success. An update has been sent to your inbox";
        } catch (InvalidUserInputException | ResourcePersistenceException | UnauthorizedException |
                 ResourceNotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping()
    public List<SaleResponse> getSalesByUser(){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        return saleService.findSalesByUserId(user.getUserId());
    }

    @GetMapping("/all")
    public List<SaleResponse> getAllSales(){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return saleService.findAllSales();
    }

    @GetMapping("/symbol/{symbol}")
    public List<SaleResponse> GetSalesBySymbol(@PathVariable String symbol){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return saleService.getSalesBySymbol(symbol.trim());
    }

    @GetMapping("/company/{company}")
    public List<SaleResponse> GetSalesByCompany(@PathVariable String company){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return saleService.getSalesByCompany(company.trim());
    }
}

