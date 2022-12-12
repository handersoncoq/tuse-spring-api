package com.tuse.tuse.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSaleRequest {
    private Long saleId;
    private Double newSellingPrice;
}
