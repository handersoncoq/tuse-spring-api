package com.tuse.tuse.services;

import com.tuse.tuse.models.Trade;
import com.tuse.tuse.models.Stock;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.TradeRepo;
import com.tuse.tuse.requests.create.BuyRequest;
import com.tuse.tuse.requests.create.SellRequest;
import com.tuse.tuse.responses.TradeResponse;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;

// TODO: redo this class
@Service
public class TradeService {

    private final TradeRepo tradeRepo;
    private final StockService stockService;

    @Autowired
    public TradeService(TradeRepo tradeRepo, StockService stockService) {
        this.tradeRepo = tradeRepo;
        this.stockService = stockService;
    }

    @Transactional
    public TradeResponse buy(BuyRequest buyRequest, User user) throws InvalidUserInputException, ResourcePersistenceException{

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Integer> invalidQuantityInput = Objects::nonNull;
        Predicate<Double> invalidBuyingPriceInput = Objects::nonNull;

        if(!notNullOrEmpty.test(buyRequest.getSymbol()))
            throw new InvalidUserInputException("No symbol was found");
        if(!invalidQuantityInput.test(buyRequest.getQuantity()) || buyRequest.getQuantity() == 0)
            throw new InvalidUserInputException("No quantity was found");
        if(!invalidBuyingPriceInput.test(buyRequest.getBuyingPrice()))
            throw new InvalidUserInputException("No buying price was found");

        Stock stock = stockService.fetchStockBySymbol(buyRequest.getSymbol());
        if(buyRequest.getQuantity() > stock.getCompany().getOutstandingShares())
            throw new UnauthorizedException("Quantity exceeds total outstanding shares");

        //  TODO: check for buying price

        Trade trade = new Trade();
        trade.setStock(stock);
        trade.setQuantity(buyRequest.getQuantity());
        trade.setAmount(buyRequest.getQuantity() * buyRequest.getBuyingPrice());
        trade.setType("buy");
        trade.setUser(user);

        if(!Objects.equals(buyRequest.getBuyingPrice(), stock.getPrice()))
            updateMarketCap(stock, buyRequest.getQuantity(), buyRequest.getBuyingPrice());

        return new TradeResponse(tradeRepo.save(trade));
    }

    @Transactional
    public TradeResponse sell(SellRequest sellRequest, User user) throws InvalidUserInputException, ResourcePersistenceException{

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Integer> invalidQuantityInput = Objects::nonNull;
        Predicate<Double> invalidBuyingPriceInput = Objects::nonNull;

        if(!notNullOrEmpty.test(sellRequest.getSymbol()))
            throw new InvalidUserInputException("No symbol was found");
        if(!invalidQuantityInput.test(sellRequest.getQuantity()) || sellRequest.getQuantity() == 0)
            throw new InvalidUserInputException("No quantity was found");
        if(!invalidBuyingPriceInput.test(sellRequest.getSellingPrice()))
            throw new InvalidUserInputException("No selling price was found");

        //  TODO: check for selling price

        // TODO: redo this method
        Stock stock = stockService.fetchStockBySymbol(sellRequest.getSymbol());
        if(sellRequest.getQuantity() > stock.getCompany().getOutstandingShares())
            throw new UnauthorizedException("Quantity exceeds total outstanding shares");


        Trade trade = new Trade();
        trade.setStock(stock);
        trade.setQuantity(sellRequest.getQuantity());
        trade.setAmount(sellRequest.getQuantity() * sellRequest.getSellingPrice());
        trade.setType("sell");
        trade.setUser(user);

        return new TradeResponse(tradeRepo.save(trade));
    }

    public void updateMarketCap(Stock stock, int quantity, double buyingPrice){

        if(buyingPrice == 0) throw new InvalidUserInputException("Invalid Buying Price");

        if (buyingPrice > stock.getPrice()){
            double excess = (buyingPrice - stock.getPrice())*quantity;
            double newMarketCap = stock.getCompany().getMarketCap() + excess;
            stock.setPrice(newMarketCap / stock.getCompany().getOutstandingShares());
            stock.getCompany().setMarketCap(newMarketCap);
            stockService.save(stock);
        }

        if (buyingPrice < stock.getPrice()){
            double deficit = (stock.getPrice() - buyingPrice)*quantity;
            double newMarketCap = stock.getCompany().getMarketCap() - deficit ;
            stock.setPrice(newMarketCap / stock.getCompany().getOutstandingShares());
            stock.getCompany().setMarketCap(newMarketCap);
            stockService.save(stock);
        }

    }

    @Transactional(readOnly = true)
    public List<TradeResponse> getOrdersByUser(Long userId){

        return tradeRepo.findOrdersByUserId(userId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<TradeResponse> findAllOrders(){

        List<Trade> trades = tradeRepo.findAll();

        List<TradeResponse> orderList = new ArrayList<>();

        for(Trade trade : trades){
            orderList.add(new TradeResponse(trade));
        }

        return orderList;
    }

    @Transactional(readOnly = true)
    public List<TradeResponse> GetOrdersBySymbol(String symbol){

        return tradeRepo.findOrdersBySymbol(symbol)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<TradeResponse> GetOrdersByCompany(String company){

        return tradeRepo.findOrdersByCompany(company)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
