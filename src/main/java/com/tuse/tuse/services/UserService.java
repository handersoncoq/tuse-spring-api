package com.tuse.tuse.services;

import com.tuse.tuse.models.Account;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.UserRepo;
import com.tuse.tuse.requests.UpdateUserRequest;
import com.tuse.tuse.requests.NewUserRequest;
import com.tuse.tuse.utilities.InvalidCredentialsException;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AccountService acctService;
    private User sessionUser;

    @Autowired
    public UserService(UserRepo userRepo,AccountService acctService ) {
        this.userRepo = userRepo;
        this.acctService = acctService;
    }

    @Transactional
    public User signUp(NewUserRequest newUserRequest)throws InvalidUserInputException, ResourcePersistenceException {

        if(newUserRequest.getUsername() == null || newUserRequest.getUsername().equals(""))
            throw new InvalidUserInputException("Username was null or empty");
        if(newUserRequest.getPassword() == null || newUserRequest.getPassword() .equals(""))
            throw new InvalidUserInputException("Password was null or empty");

        if(userRepo.fetchUsername(newUserRequest.getUsername()).isPresent())
            throw new ResourcePersistenceException("Username is not available");

        User newUser = userRepo.save(new User(newUserRequest));

        Account newUserAccount = new Account();
        newUserAccount.setUser(newUser);
        newUserAccount.setBalance(500.0);
        acctService.save(newUserAccount);

        return newUser;
    }

    @Transactional
    public void save(User user){
        userRepo.save(user);
    }

    @Transactional
    public User update(UpdateUserRequest updateRequest, User currentUser) throws InvalidUserInputException, ResourcePersistenceException{

        if(currentUser == null) throw new ResourceNotFoundException("Not Logged In");
        User updatedUser = userRepo.findById(currentUser.getUserId()).orElseThrow(ResourceNotFoundException::new);
        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");

        if(notNullOrEmpty.test(updateRequest.getUsername()))
            updatedUser.setUsername(updateRequest.getUsername());

        if(notNullOrEmpty.test(updateRequest.getPassword()))
            updatedUser.setPassword(updateRequest.getPassword());

        return userRepo.save(updatedUser);
    }

    // ----------------------------------------------------------------

    @Transactional
    public User signIn(String username, String password) throws InvalidCredentialsException, ResourceNotFoundException{
        User user = getUserByUsername(username);
        if(password.equals(user.getPassword()) && user.isActive()) {
            setSessionUser(user);
            return user;
        } else throw new InvalidCredentialsException("Incorrect password");
    }

    public void setSessionUser(User sessionUser){
        this.sessionUser = sessionUser;
    }
    public User getSessionUser() {
        return sessionUser;
    }
    @Transactional
    public void signOut(){
        setSessionUser(null);
    }

    @Transactional
    public User getUserByUsername(String username){
        return userRepo.fetchUsername(username).orElseThrow(InvalidCredentialsException::new);
    }
    @Transactional
    public void deactivate(User user){
        userRepo.deactivate(user.getUserId());
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepo.findById(userId).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepo.findAll();
    }

}
