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

    @OneToOne
    private String symbol;

    @OneToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column
    private Double price;

    @Column
    private Long volume;

    public Stock(Company company) {
        this.symbol = company.getSymbol();
        this.price = company.getInitialPrice();
    }
}
