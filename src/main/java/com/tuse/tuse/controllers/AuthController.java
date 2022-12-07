package com.tuse.tuse.controllers;

import com.tuse.tuse.models.User;
import com.tuse.tuse.requests.LoginRequest;
import com.tuse.tuse.services.UserService;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://127.0.0.1:3000"},  allowCredentials = "true")

public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String signIn(@RequestBody LoginRequest creds){

        try {
            userService.signIn(creds.getUsername(), creds.getPassword());
            return "Success";
        } catch (InvalidUserInputException | ResourcePersistenceException e) {
            return e.getMessage();
        }

    }

    @DeleteMapping
    public String signOut(){
        userService.signOut();
        return "You've signed out";
    }
}
