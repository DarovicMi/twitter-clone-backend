package com.twitter.services;
import com.twitter.config.listener.PasswordResetToken;
import com.twitter.config.listener.VerificationToken;
import com.twitter.entities.User;
import com.twitter.exceptions.*;
import com.twitter.repositories.PasswordResetTokenRepository;
import com.twitter.repositories.UserRepository;
import com.twitter.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public Boolean userExists(User user){
        if(userRepository.findByUsername(user.getUsername()) != null){
            return true;
        }
        if(userRepository.findByEmail(user.getEmail()) != null) {
            return true;
        }
        return false;
    }

    public void registerUser(User user) throws Exception {
        if(userExists(user)){
            throw new UserAlreadyRegisteredException("User already registered");
        }
        String userEmail = user.getEmail();
        String userUserName = user.getUsername();
        String userPassword = user.getPassword();
        LocalDateTime userCreationDate = user.getCreationDate();

        boolean invalidUsername = (userUserName == null);
        boolean invalidEmail = (userEmail == null);
        boolean invalidPassword = (userPassword == null);

        if(invalidPassword) throw new InvalidPasswordException("Incorrect password");
        if(invalidUsername) throw new InvalidUsernameException("Incorrect username");
        if(invalidEmail) throw new InvalidEmailException("Incorrect email");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreationDate(LocalDateTime.now());
        userRepository.save(user);
    }

    public void changePassword(User user, String newPassword) throws InvalidPasswordException {
        boolean invalidPassword = (user.getPassword() == null);
        if(invalidPassword) throw new InvalidPasswordException("Incorrect password");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void updateUser(Long userId, User user) throws UserNotFoundException {
        User updatedUser = userRepository.findById(userId).orElse(null);
        if(updatedUser == null){
            throw new UserNotFoundException(String.format("User with id = %s was not found",userId));
        }
        updatedUser.setEmail(user.getEmail());
        updatedUser.setUSER_ACCOUNT_TYPE(user.getUSER_ACCOUNT_TYPE());
        updatedUser.setImageUrl(user.getImageUrl());
        updatedUser.setUsername(user.getUsername());

        userRepository.save(updatedUser);
    }

    public void deleteUser(Long userId) throws UserNotFoundException {
       boolean deletedUser = userRepository.existsById(userId);
       if(!deletedUser){
           throw new UserNotFoundException(String.format("User with id = %s was not found",userId));
       }
       userRepository.deleteById(userId);
    }

    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null){
            return "Invalid token";
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpirationDate().getTime() - calendar.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(verificationToken);
            return "Token has expired";
        }

       userRepository.save(user);
        return "Valid";

    }

    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null){
            return "Invalid password reset token";
        }
        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if((passwordResetToken.getExpirationDate().getTime() - calendar.getTime().getTime() <= 0)){
            passwordResetTokenRepository.delete(passwordResetToken);
            return "Password Reset Token has expired";
        }
        return "Valid Password Reset Token";
    }

    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }


    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user,token);

        verificationTokenRepository.save(verificationToken);
    }
}
