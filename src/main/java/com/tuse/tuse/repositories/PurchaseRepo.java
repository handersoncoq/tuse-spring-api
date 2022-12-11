package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepo extends JpaRepository<Purchase, Long> {

    @Query(value = "FROM Purchase WHERE user_id = :userId")
    Optional<List<Purchase>> findPurchasesByUserId(@Param("userId") Long userId);

    @Query(value = "FROM Purchase WHERE user_id = :userId AND stock_id = :stockId")
    Optional<List<Purchase>> findPurchasesByUserIdAndSymbol(@Param("userId") Long userId, @Param("stockId") Long stockId);

}
