package com.tuse.tuse.services;

import com.tuse.tuse.models.Stock;
import com.tuse.tuse.repositories.StockRepo;
import com.tuse.tuse.responses.StockResponse;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<StockResponse> getAllStocks(){

        return stockRepo.findAll()
                .stream()
                .map(StockResponse::new)
                .collect(Collectors.toList());
    }

    public List<StockResponse> getStockBySymbol(String symbol){
        return stockRepo.findStockBySymbol(symbol).orElseThrow(ResourceNotFoundException::new);
    }

    public Stock fetchStockBySymbol(String symbol){
        return stockRepo.getStockBySymbol(symbol).orElseThrow(ResourceNotFoundException::new);
    }

    public List<StockResponse> filterByPriceGreaterThan(double price){
        return stockRepo.filterByPriceGreaterThan(price).orElseThrow(ResourceNotFoundException::new);
    }

    public List<StockResponse> filterByPriceLowerThan(double price){
        return stockRepo.filterByPriceLowerThan(price).orElseThrow(ResourceNotFoundException::new);
    }
}
