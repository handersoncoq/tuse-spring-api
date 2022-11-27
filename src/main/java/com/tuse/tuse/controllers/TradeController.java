package com.tuse.tuse.controllers;

import com.tuse.tuse.models.User;
import com.tuse.tuse.requests.create.BuyRequest;
import com.tuse.tuse.responses.TradeResponse;
import com.tuse.tuse.services.TradeService;
import com.tuse.tuse.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class TradeController {

    private final TradeService tradeService;
    private final UserService userService;

    public TradeController(TradeService tradeService, UserService userService) {

        this.tradeService = tradeService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public TradeResponse createOrder(@RequestBody BuyRequest buyRequest) {

        User user = userService.getSessionUser();

        if(user == null){

            User newUser = new User();
            newUser.setUsername("Tu$eUser" + UUID.randomUUID().getLeastSignificantBits());
            newUser.setPassword(String.valueOf(UUID.randomUUID()));
            newUser.setFirstName("Anonymous");
            newUser.setLastName("TU$E");
            newUser.setRegistrationDate(new Date());

            userService.save(newUser);

           return tradeService.buy(buyRequest, newUser);
        }

        return tradeService.buy(buyRequest, user);
    }

    @GetMapping()
    public List<TradeResponse> getOrdersByUser(){
        User user = userService.getSessionUser();
        return tradeService.getOrdersByUser(user.getUserId());
    }

    @GetMapping("/all")
    public List<TradeResponse> getAllOrders(){
        return tradeService.findAllOrders();
    }
}

