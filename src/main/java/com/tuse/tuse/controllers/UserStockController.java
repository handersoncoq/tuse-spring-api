package com.tuse.tuse.controllers;

import com.tuse.tuse.models.UserStock;
import com.tuse.tuse.models.User;
import com.tuse.tuse.responses.UserStockResponse;
import com.tuse.tuse.services.PortfolioService;
import com.tuse.tuse.services.UserStockService;
import com.tuse.tuse.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/userStock")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class UserStockController {

    private final UserStockService userStockService;
    private final UserService userService;
    private final PortfolioService portfolioService;

    public UserStockController(UserStockService userStockService, UserService userService, PortfolioService portfolioService) {
        this.userStockService = userStockService;
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    @GetMapping()
    public List<UserStockResponse> getUserStocks(){
        User user = userService.getSessionUser();
        List<UserStock> userStockList = userStockService.getUserStocks(user);
        return userStockList.stream()
                .map(UserStockResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userStockId}")
    public UserStockResponse getUserStockById(@PathVariable Long userStockId){
        User user = userService.getSessionUser();
        return new UserStockResponse(userStockService.getUserStockById(user, userStockId));
    }

    @GetMapping("/userPortfolio")
    public List<HashMap<String, Object>> getUserPortfolio(){
        User user = userService.getSessionUser();
        return portfolioService.getUserPortfolio(user);
    }

    @GetMapping("/{symbol}")
    public UserStock getUserStockByUserIdAndSymbol(@PathVariable String symbol){
        User user = userService.getSessionUser();
        return userStockService.getUserStockByUserIdAndSymbol(user, symbol);
    }

}
