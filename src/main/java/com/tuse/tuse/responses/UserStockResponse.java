package com.tuse.tuse.responses;

import com.tuse.tuse.models.UserStock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStockResponse {

    private Long userStocksId;
    private String symbol;
    private Integer quantity;
    private Double priceToSell;

    public UserStockResponse(UserStock userStock) {
        this.userStocksId = userStock.getUserStocksId();
        this.symbol = userStock.getSymbol();
        this.quantity = userStock.getQuantity();
        this.priceToSell = userStock.getPriceToSell();
    }
}
