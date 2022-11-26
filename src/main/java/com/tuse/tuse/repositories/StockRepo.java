package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Stock;
import com.tuse.tuse.responses.StockResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepo extends JpaRepository<Stock, Long> {

    @Query(value = "FROM Stock WHERE lower(symbol) = lower(:symbol)")
    Optional<List<StockResponse>> findStockBySymbol(@Param("symbol") String symbol);

    @Query(value = "FROM Stock WHERE lower(symbol) = lower(:symbol)")
    Optional<Stock> getStockBySymbol(@Param("symbol") String symbol);

    @Query(value = "FROM Stock WHERE price >= :price")
    Optional<List<StockResponse>> filterByPriceGreaterThan(@Param("price") double price);

    @Query(value = "FROM Stock WHERE price <= :price")
    Optional<List<StockResponse>> filterByPriceLowerThan(@Param("price") double price);
}
