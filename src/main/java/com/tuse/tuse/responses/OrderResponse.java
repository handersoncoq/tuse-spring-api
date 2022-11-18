package com.tuse.tuse.responses;

import com.tuse.tuse.models.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderResponse {


    private Long orderId;
    private String company;
    private String symbol;
    private Integer quantity;
    private double amount;
    private String username;

    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.company = order.getStock().getCompany();
        this.symbol = order.getStock().getSymbol();
        this.quantity = order.getQuantity();
        this.amount = order.getQuantity();
        this.username = order.getUser().getUsername();
    }
}
