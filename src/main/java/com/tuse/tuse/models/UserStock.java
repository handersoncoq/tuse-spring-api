package com.tuse.tuse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_stocks")

public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_stock_id")
    private Long userStockId;
    @Column
    private String symbol = "";
    @Column
    private Integer quantity;
    @Column(name ="price_to_sell")
    private Double priceToSell;
    @Column(name = "quantity_on_sale")
    private Integer quantityOnSale;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
