package com.twitter.controllers;

import com.twitter.config.event.RegistrationEvent;
import com.twitter.config.listener.VerificationToken;
import com.twitter.config.model.PasswordModel;
import com.twitter.entities.User;
import com.twitter.exceptions.InvalidPasswordException;
import com.twitter.exceptions.UserNotFoundException;
import com.twitter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user, final HttpServletRequest request) throws Exception {
         userService.registerUser(user);
         publisher.publishEvent(new RegistrationEvent(user,applicationUrl(request)));
         return "User successfully registered";
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable("id") Long id,@RequestBody User user) throws UserNotFoundException {
       userService.updateUser(id,user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }



    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam(name = "token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("Valid")){
            return "User verified successfully";
        } else {
            return "User verification failed";
        }
    }
    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam(name = "token") String oldToken, HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user,applicationUrl(request),verificationToken);
        return "A link to verify your account has been sent";
    }
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user,token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) throws InvalidPasswordException {
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("Valid Password Reset Token")){
            return "Invalid token";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password changed successfully";
        } else {
            return "Failed to change password";
        }
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;
        log.info("Click the link to reset your password: {}", url);
        return url;
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
        log.info("Click the link to verify your account: {}", url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
