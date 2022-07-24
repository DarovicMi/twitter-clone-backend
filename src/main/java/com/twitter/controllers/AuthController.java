package com.twitter.controllers;


import com.twitter.entities.User;
import com.twitter.services.UserService;
import com.twitter.verificationenums.LoginEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public LoginEnum authentication() throws Exception {
        User user = userService.authenticate();
        if(user.getUsername() != null && user.getPassword() != null){
            return LoginEnum.SUCCESS;
        } else {
            return LoginEnum.FAILURE;
        }
    }
}