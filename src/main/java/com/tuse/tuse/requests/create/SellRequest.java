package com.tuse.tuse.requests.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SellRequest {

    private String symbol;
    private Double sellingPrice;
    private Integer quantity;
}
