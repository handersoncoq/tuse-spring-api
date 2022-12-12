package com.tuse.tuse.responses;

import com.tuse.tuse.models.UserStock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStockResponse {

    private Long userStockId;
    private String symbol;
    private Integer quantity;
    private Integer quantityOnSale;
    private Double priceToSell;

    public UserStockResponse(UserStock userStock) {
        this.userStockId = userStock.getUserStockId();
        this.symbol = userStock.getSymbol();
        this.quantity = userStock.getQuantity();
        this.priceToSell = userStock.getPriceToSell();
        this.quantityOnSale = userStock.getQuantityOnSale();
    }
}
