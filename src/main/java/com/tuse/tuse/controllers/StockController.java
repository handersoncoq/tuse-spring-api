package com.tuse.tuse.controllers;

import com.tuse.tuse.models.Stock;
import com.tuse.tuse.services.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/all")
    public List<Stock> getAllStocks(){

        return stockService.getAllStocks();
    }

    @GetMapping("/symbol/{symbol}")
    public List<Stock> getStockBySymbol(@PathVariable String symbol){

        return stockService.getStockBySymbol(symbol.trim());
    }

    @GetMapping("/company/{company}")
    public Stock getStockByCompany(@PathVariable String company){

        return stockService.getStockByCompany(company.trim());
    }

    @GetMapping("/greaterThan/{price}")
    public List<Stock> filterByPriceGreaterThan(@PathVariable double price){

        return stockService.filterByPriceGreaterThan(price);
    }

    @GetMapping("/lowerThan/{price}")
    public List<Stock> filterByPriceLowerThan(@PathVariable double price){

        return stockService.filterByPriceLowerThan(price);
    }
}
