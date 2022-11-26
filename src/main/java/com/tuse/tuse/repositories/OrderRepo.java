package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Order;
import com.tuse.tuse.responses.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query(value = "FROM Order WHERE username = :username")
    Optional<List<OrderResponse>> findOrdersByUsername(@Param("username") String username);

    @Query(value = "FROM Order WHERE company = :company")
    Optional<List<OrderResponse>> findOrdersByCompany(@Param("company") String company);

    @Query(value = "FROM Order WHERE symbol = :symbol")
    Optional<List<OrderResponse>> findOrdersBySymbol(@Param("symbol") String symbol);
}
