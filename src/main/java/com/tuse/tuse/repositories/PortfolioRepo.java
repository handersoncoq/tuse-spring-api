package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {

}