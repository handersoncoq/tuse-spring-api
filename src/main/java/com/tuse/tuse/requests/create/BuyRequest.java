package com.tuse.tuse.requests.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BuyRequest {

    private String symbol;
    private Double buyingPrice;
    private Integer quantity;
}
