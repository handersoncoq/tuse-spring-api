package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Trade;
import com.tuse.tuse.responses.TradeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepo extends JpaRepository<Trade, Long> {

    @Query(value = "FROM Trade WHERE user_id = :userId")
    Optional<List<TradeResponse>> findOrdersByUserId(@Param("userId") Long userId);

    @Query(value = "FROM Trade WHERE company = :company")
    Optional<List<TradeResponse>> findOrdersByCompany(@Param("company") String company);

    @Query(value = "FROM Trade WHERE symbol = :symbol")
    Optional<List<TradeResponse>> findOrdersBySymbol(@Param("symbol") String symbol);
}
