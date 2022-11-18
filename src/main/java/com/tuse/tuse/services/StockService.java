package com.tuse.tuse.services;

import com.tuse.tuse.models.Stock;
import com.tuse.tuse.repositories.StockRepo;
import com.tuse.tuse.responses.StockResponse;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {

    private final StockRepo stockRepo;

    @Autowired
    public StockService(StockRepo stockRepo) {
        this.stockRepo = stockRepo;
    }

    @Transactional
    public Stock save(Stock stock){
        return stockRepo.save(stock);
    }

    @Transactional(readOnly = true)
    public List<Stock> getAllStocks(){
        return stockRepo.findAll();
    }

    @Transactional
    public Stock getStockBySymbol(String symbol){
        return stockRepo.findStockBySymbol(symbol).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public Stock getStockByCompany(String company){
        return stockRepo.findStockBySymbol(company).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public List<Stock> filterByPriceGreaterThan(double price){
        return stockRepo.filterByPriceGreaterThan(price).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public List<Stock> filterByPriceLowerThan(double price){
        return stockRepo.filterByPriceLowerThan(price).orElseThrow(ResourceNotFoundException::new);
    }
}
