package com.tuse.tuse.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderRequest {

    private String symbol;
    private Double buyingPrice;
    private Integer quantity;
}
