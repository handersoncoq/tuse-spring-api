package com.tuse.tuse.controllers;

import com.tuse.tuse.models.User;
import com.tuse.tuse.requests.OrderRequest;
import com.tuse.tuse.responses.OrderResponse;
import com.tuse.tuse.services.OrderService;
import com.tuse.tuse.services.UserService;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {

        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            User user = userService.getSessionUser();
            if(user == null){
               User nonSubscriber = new User();
                nonSubscriber.setUsername("Tu$eUser" + UUID.randomUUID().getLeastSignificantBits());
                nonSubscriber.setPassword(String.valueOf(UUID.randomUUID().getLeastSignificantBits()));
                userService.save(nonSubscriber);
                orderService.save(orderRequest, nonSubscriber);
                return "Success";
            }
            orderService.createOrder(orderRequest, user);
            return "Success";
        } catch (InvalidUserInputException | ResourcePersistenceException | UnauthorizedException |
                 ResourceNotFoundException e) {
            return e.getMessage();
        }
    }

    @GetMapping()
    public List<OrderResponse> getOrdersByUser(){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        return orderService.findOrdersByUserId(user.getUserId());
    }

    @GetMapping("/all")
    public List<OrderResponse> getAllOrders(){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return orderService.findAllOrders();
    }

    @GetMapping("/symbol/{symbol}")
    public List<OrderResponse> GetOrdersBySymbol(@PathVariable String symbol){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return orderService.GetOrdersBySymbol(symbol.trim());
    }

    @GetMapping("/company/{company}")
    public List<OrderResponse> GetOrdersByCompany(@PathVariable String company){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return orderService.GetOrdersByCompany(company.trim());
    }
}

