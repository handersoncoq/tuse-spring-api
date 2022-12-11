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
    @Column(name = "user_stocks_id")
    private Long userStocksId;
    @Column
    private String symbol = "";
    @Column
    private Integer quantity;
    @Column(name ="upper_selling_price")
    private Double priceToSell;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
