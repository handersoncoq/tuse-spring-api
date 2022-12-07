package com.tuse.tuse.controllers;

import com.tuse.tuse.models.User;
import com.tuse.tuse.requests.NewUserRequest;
import com.tuse.tuse.requests.UpdateUserRequest;
import com.tuse.tuse.services.UserService;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public User signUp(@RequestBody NewUserRequest newUserRequest){

        return userService.signUp(newUserRequest);
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK)
    public User update(@RequestBody UpdateUserRequest updateRequest){
        User sessionUser = userService.getSessionUser();
        if(sessionUser != null) return userService.update(updateRequest, sessionUser);
        else throw new UnauthorizedException("Unauthorized. Target user not logged in");
    }

    @PutMapping("/deactivate")
    @ResponseStatus(value = HttpStatus.OK)
    public String deactivate(){
        User sessionUser = userService.getSessionUser();
        if(sessionUser != null){
            userService.deactivate(sessionUser);
            userService.logOut();
            return "Your account has been deactivated";
        } else return "Unauthorized. Target user not logged in";
    }

    @GetMapping("/all")
    public List<User> findAll(){
        User user = userService.getSessionUser();
        if(user==null) throw new ResourcePersistenceException("No user was found");
        if(!user.isAdmin()) throw new UnauthorizedException("User not admin");
        return userService.findAll();
    }

    @GetMapping("/id/{id}")
    public User findById(@PathVariable Long id){
        return userService.findById(id);
    }

    @GetMapping("/username/{username}")
    public User findByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping
    public User findCurrentUser(){
        User sessionUser = userService.getSessionUser();
        if(sessionUser != null) return userService.findById(sessionUser.getUserId());
        else throw new UnauthorizedException("Unauthorized. You have not logged in");
    }
}
