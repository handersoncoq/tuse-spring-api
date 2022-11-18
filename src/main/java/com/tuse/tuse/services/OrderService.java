package com.tuse.tuse.services;

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

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final StockService stockService;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepo orderRepo, StockService stockService, UserService userService) {
        this.orderRepo = orderRepo;
        this.stockService = stockService;
        this.userService = userService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) throws InvalidUserInputException, ResourcePersistenceException{

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Integer> invalidQuantityInput = Objects::nonNull;
        Predicate<Double> invalidBuyingPriceInput = Objects::nonNull;

        if(!notNullOrEmpty.test(orderRequest.getSymbol()))
            throw new InvalidUserInputException("No symbol was found");
        if(!invalidQuantityInput.test(orderRequest.getQuantity()) || orderRequest.getQuantity() == 0)
            throw new InvalidUserInputException("No quantity was found");

        Stock stock = stockService.getStockBySymbol(orderRequest.getSymbol());
        if(orderRequest.getQuantity() > stock.getTotalShares())
            throw new UnauthorizedException("Quantity exceeds the total shares available");

        User user = new User();
        userService.save(user);

        Order order = new Order();
        order.setStock(stock);
        order.setQuantity(orderRequest.getQuantity());

        if(invalidBuyingPriceInput.test(orderRequest.getBuyingPrice()) && orderRequest.getBuyingPrice() != 0){
            order.setAmount(orderRequest.getQuantity() * orderRequest.getBuyingPrice());
            updateMarketCap(stock, orderRequest.getQuantity(), orderRequest.getBuyingPrice());
        } else order.setAmount(orderRequest.getQuantity() * stock.getPrice());

        order.setUser(user);

        return new OrderResponse(orderRepo.save(order));
    }

    public void updateMarketCap(Stock stock, int quantity, double buyingPrice){

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
    public List<OrderResponse> getOrdersByUsername(String username){

        return orderRepo.findOrdersByUsername(username)
                .orElseThrow(ResourceNotFoundException::new);
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

        return orderRepo.findOrdersByCompany(symbol)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> GetOrdersByCompany(String company){

        return orderRepo.findOrdersByCompany(company)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
