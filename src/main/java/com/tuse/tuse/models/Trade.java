package com.tuse.tuse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")

public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    @Column
    private Integer quantity;
    @Column
    private Double amount;
    @Column
    private String type;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
