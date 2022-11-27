package com.tuse.tuse.services;

import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.UserRepo;
import com.tuse.tuse.requests.create.NewUserRequest;
import com.tuse.tuse.requests.update.UpdateUserRequest;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class UserService {

    private final UserRepo userRepo;
    private User sessionUser;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User register(NewUserRequest userRequest) throws InvalidUserInputException, ResourcePersistenceException {

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");

        if(!notNullOrEmpty.test(userRequest.getFirstName()))
            throw new InvalidUserInputException("First name is empty");
        if(!notNullOrEmpty.test(userRequest.getLastName()))
            throw new InvalidUserInputException("First name is empty");
        if(!notNullOrEmpty.test(userRequest.getPassword()))
            throw new InvalidUserInputException("First name is empty");
        if(!notNullOrEmpty.test(userRequest.getUsername()))
            throw new InvalidUserInputException("First name is empty");

        isUsernameAvailable(userRequest.getUsername());

        User user = new User(userRequest);
        user.setRegistrationDate(new Date());

        return userRepo.save(user);
    }

    public void save(User user){
        userRepo.save(user);
    }

    @Transactional
    public User update(UpdateUserRequest updateRequest, User currentUser) throws InvalidUserInputException{

        User updatedUser = userRepo.findById(currentUser.getUserId()).orElseThrow(ResourceNotFoundException::new);
        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");

        if(notNullOrEmpty.test(updateRequest.getFirstName()))
            updatedUser.setFirstName(updateRequest.getFirstName());

        if(notNullOrEmpty.test(updateRequest.getLastName()))
            updatedUser.setLastName(updateRequest.getLastName());

        if(notNullOrEmpty.test(updateRequest.getUsername())){
            isUsernameAvailable(updateRequest.getUsername());
            updatedUser.setUsername(updateRequest.getUsername());
        }
        if(notNullOrEmpty.test(updateRequest.getPassword()))
            updatedUser.setPassword(updateRequest.getPassword());

        return userRepo.save(updatedUser);

    }
    public User logIn(String username, String password){
        User user = getUserByUsername(username);
        if(Objects.equals(password, user.getPassword()) && user.isActive()){
            setSessionUser(user);
            return user;
        }
        else throw new InvalidUserInputException("Invalid Password");

    }
    public void logOut(){
        setSessionUser(null);
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
    }

    public User getSessionUser(){
        return sessionUser;
    }

    private User getUserByUsername(String username){
        return userRepo.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
    }

    // ******************************************************************************************* //

    private void isUsernameAvailable(String username) {

        if(userRepo.findByUsername(username).isPresent())
            throw new ResourcePersistenceException("Username is not valid");
    }
}
