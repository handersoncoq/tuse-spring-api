package com.tuse.tuse.services;

import com.tuse.tuse.models.Account;
import com.tuse.tuse.models.Order;
import com.tuse.tuse.models.Stock;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.OrderRepo;
import com.tuse.tuse.requests.OrderRequest;
import com.tuse.tuse.responses.OrderResponse;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final AccountService acctService;
    private final StockService stockService;

    @Autowired
    public OrderService(OrderRepo orderRepo, StockService stockService, AccountService acctService) {
        this.orderRepo = orderRepo;
        this.stockService = stockService;
        this.acctService = acctService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, User user) throws InvalidUserInputException, ResourcePersistenceException{

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Integer> invalidQuantityInput = Objects::nonNull;
        Predicate<Double> invalidBuyingPriceInput = Objects::nonNull;

        if(!notNullOrEmpty.test(orderRequest.getSymbol()))
            throw new InvalidUserInputException("No symbol was found");
        if(!invalidQuantityInput.test(orderRequest.getQuantity()) || orderRequest.getQuantity() == 0)
            throw new InvalidUserInputException("No quantity was found");
        if(!invalidBuyingPriceInput.test(orderRequest.getBuyingPrice()))
            throw new InvalidUserInputException("No buying price was found");

        Stock stock = stockService.getStockBySymbol(orderRequest.getSymbol()).get(0);
        if(orderRequest.getQuantity() > stock.getTotalShares())
            throw new UnauthorizedException("Quantity exceeds the total shares available");

        Order order = new Order();
        order.setStock(stock);
        order.setQuantity(orderRequest.getQuantity());

        Double amount = orderRequest.getQuantity() * orderRequest.getBuyingPrice();
        Account userAccount = acctService.getAccountByUserId(user.getUserId());
        if(amount > userAccount.getBalance()) throw new UnauthorizedException("Insufficient fund");

        order.setAmount(amount);
        order.setUser(user);

        userAccount.setBalance(userAccount.getBalance()-amount);
        acctService.save(userAccount);


        if(!Objects.equals(orderRequest.getBuyingPrice(), stock.getPrice()))
            updateMarketCap(stock, orderRequest.getQuantity(), orderRequest.getBuyingPrice());

        return new OrderResponse(orderRepo.save(order));
    }

    public void updateMarketCap(Stock stock, int quantity, double buyingPrice){

        if(buyingPrice == 0) throw new InvalidUserInputException("Invalid Buying Price");

        if (buyingPrice > stock.getPrice()){
            double excess = (buyingPrice - stock.getPrice())*quantity;
            double newMarketCap = stock.getMarketCap() + excess;
            stock.setPrice(newMarketCap / stock.getTotalShares());
            stock.setMarketCap(newMarketCap);
            stockService.save(stock);
        }

        if (buyingPrice < stock.getPrice()){
            double deficit = (stock.getPrice() - buyingPrice)*quantity;
            double newMarketCap = stock.getMarketCap() - deficit ;
            stock.setPrice(newMarketCap / stock.getTotalShares());
            stock.setMarketCap(newMarketCap);
            stockService.save(stock);
        }

    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrdersByUserId(Long userId){
        List<Order> orders = orderRepo.findOrdersByUserId(userId)
                .orElseThrow(ResourceNotFoundException::new);

        List<OrderResponse> orderList = new ArrayList<>();

        for(Order order : orders){
            orderList.add(new OrderResponse(order));
        }

        return orderList;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders(){

        List<Order> orders = orderRepo.findAll();

        List<OrderResponse> orderList = new ArrayList<>();

        for(Order order : orders){
            orderList.add(new OrderResponse(order));
        }

        return orderList;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> GetOrdersBySymbol(String symbol){
        List<OrderResponse> orders = findAllOrders();
       return orders.stream()
               .filter(order -> order.getSymbol().equalsIgnoreCase(symbol))
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> GetOrdersByCompany(String company){
        List<OrderResponse> orders = findAllOrders();
        return orders.stream()
                .filter(order -> order.getCompany().equalsIgnoreCase(company))
                .collect(Collectors.toList());
    }

    // save order from non-subscribers
    @Transactional
    public OrderResponse save(OrderRequest orderRequest, User user) throws InvalidUserInputException, ResourcePersistenceException{


        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Integer> invalidQuantityInput = Objects::nonNull;
        Predicate<Double> invalidBuyingPriceInput = Objects::nonNull;

        if(!notNullOrEmpty.test(orderRequest.getSymbol()))
            throw new InvalidUserInputException("No symbol was found");
        if(!invalidQuantityInput.test(orderRequest.getQuantity()) || orderRequest.getQuantity() == 0)
            throw new InvalidUserInputException("No quantity was found");
        if(!invalidBuyingPriceInput.test(orderRequest.getBuyingPrice()))
            throw new InvalidUserInputException("No buying price was found");

        Stock stock = stockService.getStockBySymbol(orderRequest.getSymbol()).get(0);
        if(orderRequest.getQuantity() > stock.getTotalShares())
            throw new UnauthorizedException("Quantity exceeds the total shares available");

        Order order = new Order();
        order.setStock(stock);
        order.setQuantity(orderRequest.getQuantity());

        Double amount = orderRequest.getQuantity() * orderRequest.getBuyingPrice();

        order.setAmount(amount);
        order.setUser(user);

        return new OrderResponse(orderRepo.save(order));

    }

}
