package com.tuse.tuse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sales")

public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    @Column
    private Integer quantity;
    @Column(name = "selling_price")
    private Double sellingPrice;
    @Column
    private Double amount;
    @Column(name = "sale_date")
    private Date saleDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
