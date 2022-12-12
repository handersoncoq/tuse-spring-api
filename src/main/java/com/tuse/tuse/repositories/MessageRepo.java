package com.tuse.tuse.repositories;

import com.tuse.tuse.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query(value = "FROM Message WHERE user_id = :userId")
    List<Message> findMessagesByUserId(@Param("userId") Long userId);

}
