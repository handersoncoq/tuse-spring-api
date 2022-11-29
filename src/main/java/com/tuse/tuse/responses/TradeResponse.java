package com.tuse.tuse.responses;

import com.tuse.tuse.models.Trade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TradeResponse {

    private Long tradeId;
    private String company;
    private String symbol;
    private Integer quantity;
    private Double amount;
    private String type;
    private String username;

    public TradeResponse(Trade trade) {
        this.tradeId = trade.getTradeId();
        this.company = trade.getStock().getCompany().getName();
        this.symbol = trade.getStock().getSymbol();
        this.quantity = trade.getQuantity();
        this.amount = trade.getAmount();
        this.type = trade.getType();
        this.username = trade.getUser().getUsername();
    }
}
