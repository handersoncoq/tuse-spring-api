package com.tuse.tuse.services;

import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User save(User user){
        return userRepo.save(user);
    }
}
