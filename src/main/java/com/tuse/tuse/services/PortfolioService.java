package com.tuse.tuse.services;

import com.tuse.tuse.models.Purchase;
import com.tuse.tuse.models.Stock;
import com.tuse.tuse.models.User;
import com.tuse.tuse.models.UserStock;
import com.tuse.tuse.repositories.UserStockRepo;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PortfolioService {

    private final UserStockRepo userStockRepo;
    private final StockService stockService;
    private final PurchaseService purchaseService;

    @Autowired
    public PortfolioService(UserStockRepo userStockRepo, StockService stockService, PurchaseService purchaseService) {

        this.userStockRepo = userStockRepo;
        this.stockService = stockService;
        this.purchaseService = purchaseService;
    }

    @Transactional
    public List<HashMap<String, Object>> getUserPortfolio(User user){

        if(user == null) throw new ResourceNotFoundException("No user was found");

        List<UserStock> userStockList = userStockRepo
                .findUserStocksByUserId(user.getUserId())
                .orElseThrow(ResourceNotFoundException::new);

        List<HashMap<String, Object>> userPortfolio = new ArrayList<>();

        for(UserStock userStock : userStockList){

            HashMap<String, Object> portfolio = new HashMap<>();

            String symbol = userStock.getSymbol();
            Integer shares = userStock.getQuantity();
            Stock stock = stockService.getBySymbol(symbol);
            Double equity = shares * stock.getPrice();

            List<Purchase> purchaseList = purchaseService.getPurchasesByUserIdAndSymbol(user.getUserId(), symbol);
            Double sumOfBuyingPrice = 0.0;
            Integer numberOfPurchase = purchaseList.size();
            for(Purchase purchase : purchaseList){
                sumOfBuyingPrice += purchase.getBuyingPrice();
            }
            Double avgCost = sumOfBuyingPrice/numberOfPurchase;
            Double totalReturn = equity - (shares * avgCost);

            portfolio.put("symbol", symbol);
            portfolio.put("shares", shares);
            portfolio.put("avgCost", avgCost);
            portfolio.put("equity", equity);
            portfolio.put("totalReturn", totalReturn);

            userPortfolio.add(portfolio);
        }
        return userPortfolio;
    }
}
