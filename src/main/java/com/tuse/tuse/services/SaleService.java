package com.tuse.tuse.services;

import com.tuse.tuse.models.*;
import com.tuse.tuse.repositories.SaleRepo;
import com.tuse.tuse.requests.SaleRequest;
import com.tuse.tuse.responses.SaleResponse;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private final StockService stockService;
    private final UserStockService userStockService;
    private final SaleRepo saleRepo;
    private final MessageService msgService;

    @Autowired
    public SaleService(SaleRepo saleRepo, StockService stockService, UserStockService userStockService, MessageService msgService) {
        this.saleRepo = saleRepo;
        this.stockService = stockService;
        this.userStockService = userStockService;
        this.msgService = msgService;
    }

    @Transactional
    public void execute(SaleRequest saleRequest, User user) throws InvalidUserInputException, ResourcePersistenceException{

        if(user == null) throw new ResourceNotFoundException("No sign-in user found");

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Integer> nonNullQuantity = Objects::nonNull;
        Predicate<Double> nonNullBuyingPrice = Objects::nonNull;

        if(!notNullOrEmpty.test(saleRequest.getSymbol()))
            throw new InvalidUserInputException("No symbol was found");
        if(!nonNullQuantity.test(saleRequest.getQuantity()) || saleRequest.getQuantity() == 0)
            throw new InvalidUserInputException("No quantity was found");
        if(!nonNullBuyingPrice.test(saleRequest.getSellingPrice()))
            throw new InvalidUserInputException("No buying price was found");

        UserStock userStock = userStockService.getUserStockByUserIdAndSymbol(user, saleRequest.getSymbol());

        if(saleRequest.getQuantity() > userStock.getQuantity())
            throw new InvalidUserInputException("You do not have enough shares for this sale");

        Stock stock = stockService.getBySymbol(saleRequest.getSymbol());

        Sale sale = new Sale();
        sale.setStock(stock);
        sale.setQuantity(saleRequest.getQuantity());
        sale.setSellingPrice(saleRequest.getSellingPrice());
        sale.setAmount(saleRequest.getSellingPrice() * saleRequest.getQuantity());
        sale.setSaleDate(new Date());
        sale.setUser(user);

        userStock.setPriceToSell(saleRequest.getSellingPrice());

        if(nonNullQuantity.test(userStock.getQuantityOnSale()))
            userStock.setQuantityOnSale(userStock.getQuantityOnSale() + saleRequest.getQuantity());

        userStock.setQuantityOnSale(saleRequest.getQuantity());
        userStock.setQuantity(userStock.getQuantity() - saleRequest.getQuantity());
        userStockService.save(userStock);

        Message msg = new Message();
        msg.setTitle("Sale Update");
        msg.setContent("The submission of the sale of the stock '"+ saleRequest.getSymbol()+"' was successful, check later for status.");
        msg.setSendDate(new Date());
        msg.setToUser(user);
        msgService.save(msg);

        saleRepo.save(sale);
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> findSalesByUserId(Long userId){
        List<Sale> sales = saleRepo.findSalesByUserId(userId)
                .orElseThrow(ResourceNotFoundException::new);

        List<SaleResponse> saleList = new ArrayList<>();

        for(Sale sale : sales){
            saleList.add(new SaleResponse(sale));
        }

        return saleList;
    }

    @Transactional
    public List<Sale> getSalesByUserId(Long userId){
        return saleRepo.findSalesByUserId(userId).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public List<Sale> getSalesByUserIdAndSymbol(Long userId, String symbol){
        Stock stock = stockService.getBySymbol(symbol);
        return saleRepo.findSalesByUserIdAndSymbol(userId, stock.getStockId())
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> findAllSales(){

        List<Sale> sales = saleRepo.findAll();

        List<SaleResponse> list = new ArrayList<>();

        for(Sale sale : sales){
            list.add(new SaleResponse(sale));
        }

        return list;
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesBySymbol(String symbol){
        List<SaleResponse> allSales = findAllSales();
       return allSales.stream()
               .filter(purchase -> purchase.getSymbol().equalsIgnoreCase(symbol))
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByCompany(String company){
        List<SaleResponse> allSales = findAllSales();
        return allSales.stream()
                .filter(purchase -> purchase.getCompany().equalsIgnoreCase(company))
                .collect(Collectors.toList());
    }

}
