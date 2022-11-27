package com.tuse.tuse.models;

import com.tuse.tuse.requests.create.NewCompanyRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "companies")

public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column
    private String name;

    @Column
    private String symbol;

    @Column(name = "initial_price")
    private Double initialPrice;

    @Column(name = "outstanding_shares")
    private Long outstandingShares;

    @Column(name = "market_cap")
    private Double marketCap;

    @Column
    private String sector;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "date_listed")
    private Date dateListed;

    public Company(NewCompanyRequest companyRequest) {
        this.name = companyRequest.getName();
        this.symbol = companyRequest.getSymbol();
        this.initialPrice = companyRequest.getInitialPrice();
        this.outstandingShares = companyRequest.getOutstandingShares();
        this.sector = companyRequest.getSector();
    }
}
