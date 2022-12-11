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

    @Query(value = "FROM UserStock WHERE user_id = :userId AND symbol = :symbol")
    Optional<UserStock> findUserStocksByUserIdAndSymbol(@Param("userId") Long userId, @Param("symbol") String symbol);
}
