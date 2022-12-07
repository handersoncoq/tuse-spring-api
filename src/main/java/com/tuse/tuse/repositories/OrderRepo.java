package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query(value = "FROM Order WHERE user_id = :userId")
    Optional<List<Order>> findOrdersByUserId(@Param("userId") Long userId);

}
