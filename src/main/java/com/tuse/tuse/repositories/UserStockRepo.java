package com.tuse.tuse.repositories;

import com.tuse.tuse.models.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStockRepo extends JpaRepository<UserStock, Long> {

    @Query(value = "FROM UserStock WHERE user_id = :userId")
    Optional<List<UserStock>> findUserStocksByUserId(@Param("userId") Long userId);

    @Query(value = "FROM UserStock WHERE user_id = :userId AND quantity != 0")
    Optional<List<UserStock>> findNonZeroUserStocksByUserId(@Param("userId") Long userId);

    @Query(value = "FROM UserStock WHERE user_id = :userId AND symbol = :symbol")
    Optional<UserStock> findUserStockByUserIdAndSymbol(@Param("userId") Long userId, @Param("symbol") String symbol);

    @Query(value = "FROM UserStock WHERE symbol = :symbol AND quantity_on_sale >= :buyingQuantity AND price_to_sell <= :buyingPrice AND user_id != :buyingUserId")
    List<UserStock> findUserStocksBySymbolQuantityPrice(@Param("symbol") String symbol, @Param("buyingQuantity") Integer buyingQuantity, @Param("buyingPrice") Double buyingPrice, @Param("buyingUserId") Long buyingUserId);
}
