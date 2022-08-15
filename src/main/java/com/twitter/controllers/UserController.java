package com.twitter.controllers;

import com.twitter.entities.User;
import com.twitter.event.PasswordEvent;
import com.twitter.event.RegistrationEvent;
import com.twitter.event.TokenEvent;
import com.twitter.exceptions.InvalidPasswordException;
import com.twitter.exceptions.UserNotFoundException;
import com.twitter.model.PasswordModel;
import com.twitter.model.PasswordResetEmailVerification;
import com.twitter.repositories.PasswordResetTokenRepository;
import com.twitter.services.UserAuthenticationService;
import com.twitter.services.UserService;
import com.twitter.verificationenums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import static com.twitter.verificationenums.PasswordChangeState.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;


    @PostMapping("/register")
    public User registerUser(@RequestBody User user, final HttpServletRequest request) throws Exception {
        userService.registerUser(user);
        publisher.publishEvent(new RegistrationEvent(user, applicationUrl(request)));
        return user;
    }


    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) throws UserNotFoundException {
        userService.updateUser(id, user);
        return user;
    }

    @GetMapping("/user/{userId}")
    public User findUserById(@PathVariable("userId") Long userId) throws UserNotFoundException {
        return userService.findUserById(userId);
    }
    @GetMapping("/user/username/{username}")
    public User findUserByUsername(@PathVariable("username") String username) {
        return userService.findUserByUsername(username);
    }

    @GetMapping("/user/email/{email}")
    public User findUserByEmail(@PathVariable("email") String email) {
        return userService.findUserByEmail(email);
    }


    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }


    @GetMapping("/verifyRegistration")
    public TokenStatusEnum verifyRegistration(@RequestParam(name = "token") String token) {
        TokenStatusEnum result = userService.validateVerificationToken(token);
        if (!result.equals(TokenStatusEnum.VALID_TOKEN)) {
            result = TokenStatusEnum.INVALID_TOKEN;
        }
        return result;
    }

    @GetMapping("/resendVerifyToken")
    public LinkEnum resendVerificationToken(@RequestParam(name = "token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        publisher.publishEvent(new TokenEvent(user, verificationToken, applicationUrl(request)));
        return LinkEnum.LINK_SENT;
    }

    @PostMapping("/resetPassword")
    public void resetPassword(@RequestBody PasswordResetEmailVerification passwordResetEmailVerification, HttpServletRequest request) {
        PasswordResetToken passwordResetToken = userService.validateOrGeneratePasswordResetToken(passwordResetEmailVerification.getEmail());
        publisher.publishEvent(new PasswordEvent(passwordResetToken.getUser(), applicationUrl(request), passwordResetToken));
    }

    @PostMapping("/savePassword")
    public ResponseEntity<PasswordChangeState> savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) throws InvalidPasswordException, UserNotFoundException {
        TokenStatusEnum result = userService.validatePasswordResetToken(token);

        if (!result.equals(TokenStatusEnum.VALID_TOKEN)) {
            return new ResponseEntity<>(INVALID_TOKEN_FOR_CHANGING_PASSWORD, HttpStatus.BAD_REQUEST);
        }
        User user = userService.getUserByPasswordResetToken(token);

        if (user != null) {

            passwordModel.setToken(userService.getUserPasswordToken(user.getId()).getToken());
            userService.changePassword(user, passwordModel.getNewPassword());
            passwordResetTokenRepository.delete(userService.getUserPasswordToken(user.getId()));
            return new ResponseEntity<>(PASSWORD_CHANGED_SUCCESSFULLY, HttpStatus.OK);

        }
        return new ResponseEntity<>(FAILED_TO_CHANGE_PASSWORD, HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/changePassword")
    public ResponseEntity<PasswordChangeState> changePassword(@RequestBody PasswordModel passwordModel) throws InvalidPasswordException {
        User user = userAuthenticationService.getLoggedInUser();
        if(user != null && passwordEncoder.matches(passwordModel.getOldPassword(), user.getPassword())) {
            userService.changeUserPassword(user, passwordModel.getNewPassword());
            return new ResponseEntity<>(PASSWORD_CHANGED_SUCCESSFULLY, HttpStatus.OK);
        }
        return new ResponseEntity<>(FAILED_TO_CHANGE_PASSWORD, HttpStatus.BAD_REQUEST);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
