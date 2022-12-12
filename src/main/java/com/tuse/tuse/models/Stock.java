package com.tuse.tuse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stocks")

public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long stockId;
    @Column
    private String company;
    @Column
    private String symbol;
    @Column
    private Double price;
    @Column
    private Double trend;
    @Column(name = "total_shares")
    private Long totalShares;
    @Column(name = "market_cap")
    private double marketCap;
    @Column
    private Long volume;

}
