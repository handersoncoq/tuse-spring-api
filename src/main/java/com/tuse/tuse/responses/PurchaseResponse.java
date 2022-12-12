package com.tuse.tuse.responses;

import com.tuse.tuse.models.Purchase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseResponse {

    private Long purchaseId;
    private String company;
    private String symbol;
    private Integer quantity;
    private Double buyingPrice;
    private Double amount;
    private Date purchaseDate;
    private String username;

    public PurchaseResponse(Purchase purchase) {
        this.purchaseId = purchase.getPurchaseId();
        this.company = purchase.getStock().getCompany();
        this.symbol = purchase.getStock().getSymbol();
        this.quantity = purchase.getQuantity();
        this.amount = purchase.getAmount();
        this.purchaseDate = purchase.getPurchaseDate();
        this.username = purchase.getUser().getUsername();
        this.buyingPrice = purchase.getBuyingPrice();
    }
}
