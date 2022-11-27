package com.tuse.tuse.requests.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompanyRequest {

    private String name;
    private String symbol;
    private Double initialPrice;
    private Long outstandingShares;
    private String sector;

}
