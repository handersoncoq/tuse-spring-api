package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Company;
import com.tuse.tuse.responses.StockResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepo  extends JpaRepository<Company, Long> {

    @Query(value = "FROM Company WHERE lower(name) = lower(:name)")
    Optional<List<Company>> findCompanyByName(@Param("name") String name);

    @Query(value = "FROM Company WHERE lower(symbol) = lower(:symbol)")
    Optional<List<Company>> findCompanyBySymbol(@Param("symbol") String symbol);

    @Query(value = "FROM Company WHERE marketCap >= :marketCap")
    Optional<List<Company>> filterByMarketCapGreaterThan(@Param("marketCap") double marketCap);

    @Query(value = "FROM Stock WHERE marketCap <= :marketCap")
    Optional<List<Company>> filterByMarketCapLowerThan(@Param("marketCap") double marketCap);
}
