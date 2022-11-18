package com.tuse.tuse.controllers;

import com.tuse.tuse.models.User;
import com.tuse.tuse.requests.OrderRequest;
import com.tuse.tuse.responses.OrderResponse;
import com.tuse.tuse.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping()
    public List<OrderResponse> getOrdersByUsername(HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        return orderService.getOrdersByUsername(user.getUsername());
    }

    @GetMapping("/all")
    public List<OrderResponse> getAllOrders(){
        return orderService.findAllOrders();
    }
}

