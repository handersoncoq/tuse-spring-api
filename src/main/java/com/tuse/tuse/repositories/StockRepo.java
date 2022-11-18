package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepo extends JpaRepository<Stock, Long> {

    @Query(value = "FROM Stock WHERE symbol = :symbol")
    Optional<Stock> findStockBySymbol(String symbol);

    @Query(value = "FROM Stock WHERE company = :company")
    Optional<Stock> findStockByCompany(String company);

    @Query(value = "FROM Stock WHERE price >= :price")
    Optional<List<Stock>> filterByPriceGreaterThan(double price);

    @Query(value = "FROM Stock WHERE price <= :price")
    Optional<List<Stock>> filterByPriceLowerThan(double price);
}
