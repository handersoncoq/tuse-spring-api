package com.tuse.tuse.responses;

import com.tuse.tuse.models.Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SaleResponse {

    private Long saleId;
    private String company;
    private String symbol;
    private Integer quantity;
    private Double sellingPrice;
    private Double amount;
    private String username;

    public SaleResponse(Sale sale) {
        this.saleId = sale.getSaleId();
        this.company = sale.getStock().getCompany();
        this.symbol = sale.getStock().getSymbol();
        this.quantity = sale.getQuantity();
        this.amount = sale.getAmount();
        this.username = sale.getUser().getUsername();
        this.sellingPrice = sale.getSellingPrice();
    }
}
