package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepo extends JpaRepository<Sale, Long> {

    @Query(value = "FROM Sale WHERE user_id = :userId")
    Optional<List<Sale>> findSalesByUserId(@Param("userId") Long userId);

    @Query(value = "FROM Sale WHERE user_id = :userId AND stock_id = :stockId")
    Optional<List<Sale>> findSalesByUserIdAndSymbol(@Param("userId") Long userId, @Param("stockId") Long stockId);

}
