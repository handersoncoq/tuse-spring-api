package com.tuse.tuse.requests.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SellRequest {

    private String symbol;
    private Double lowerPrice;
    private Double upperPrice;
    private Integer quantity;
}
