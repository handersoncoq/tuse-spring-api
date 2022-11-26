package com.tuse.tuse.responses;

import com.tuse.tuse.models.Company;
import com.tuse.tuse.models.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {

    private Long stockId;
    private String symbol;
    private String company;
    private Double price;
    private Long volume;

    public StockResponse(Stock stock) {
        this.stockId = stock.getStockId();
        this.symbol = stock.getSymbol();
        this.company = stock.getCompany().getName();
        this.price = stock.getPrice();
        this.volume = stock.getVolume();
    }
}
