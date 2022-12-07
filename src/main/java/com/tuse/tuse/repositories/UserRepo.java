package com.tuse.tuse.repositories;

import com.tuse.tuse.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    @Query(value = "FROM User where lower(username) = lower(:username)")
    Optional<User> fetchUsername(String username);

    @Modifying
    @Query("UPDATE User SET is_active = false WHERE id = :userId")
    void deactivate(Long userId);
}