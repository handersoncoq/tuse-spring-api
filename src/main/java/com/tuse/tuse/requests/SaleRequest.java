package com.tuse.tuse.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SaleRequest {

    private String symbol;
    private Double sellingPrice;
    private Integer quantity;
}
