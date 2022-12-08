package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    @Query(value = "FROM Account WHERE user_id = :userId")
    Optional<Account> findAccountByUserId(@Param("userId") Long userId);
}
