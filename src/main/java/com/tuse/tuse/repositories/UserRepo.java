package com.tuse.tuse.repositories;

import com.tuse.tuse.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends CrudRepository<User, String> {

}