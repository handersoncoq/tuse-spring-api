package com.tuse.tuse.responses;

import com.tuse.tuse.models.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CompanyResponse {

    private Long companyId;
    private String name;
    private String symbol;
    private Double initialPrice;
    private Long outstandingShares;
    private Double marketCap;
    private String sector;
    private String owner;
    private Date dateListed;

    public CompanyResponse(Company company) {
        this.companyId = company.getCompanyId();
        this.name = company.getName();
        this.symbol = company.getSymbol();
        this.initialPrice = company.getInitialPrice();
        this.outstandingShares = company.getOutstandingShares();
        this.marketCap = company.getMarketCap();
        this.sector = company.getSector();
        this.owner = company.getOwner().getUsername();
        this.dateListed = company.getDateListed();
    }
}
