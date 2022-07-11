package com.twitter.services;

import com.twitter.entities.User;
import com.twitter.exceptions.InvalidEmailException;
import com.twitter.exceptions.InvalidPasswordException;
import com.twitter.exceptions.InvalidUsernameException;
import com.twitter.exceptions.UserAlreadyRegisteredException;
import com.twitter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Boolean userExists(User user){
        if(userRepository.findByUsername(user.getUsername()) != null){
            return true;
        }
        if(userRepository.findByEmail(user.getEmail()) != null) {
            return true;
        }
        return false;
    }
    public User registerUser(User user) throws Exception {
        if(userExists(user)){
            throw new UserAlreadyRegisteredException("User already registered");
        }
        String userEmail = user.getEmail();
        String userUserName = user.getUsername();
        String userPassword = user.getPassword();

        boolean invalidUsername = (userUserName == null) || !userUserName.matches("[A-Za-z0-9_]+");
        boolean invalidEmail = (userEmail == null) || !userEmail.matches("[A-Za-z0-9_]+@[A-Za-z0-9.]+");
        boolean invalidPassword = (userPassword == null) || !userPassword.matches("[A-Za-z0-9@#]+");

        if(invalidPassword) throw new InvalidPasswordException("Incorrect password");
        if(invalidUsername) throw new InvalidUsernameException("Incorrect username");
        if(invalidEmail) throw new InvalidEmailException("Incorrect email");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public void changePassword(User user, String newPassword) throws InvalidPasswordException {
        boolean invalidPassword = (user.getPassword() == null) || !user.getPassword().matches("[A-Za-z0-9@#]+");
        if(invalidPassword) throw new InvalidPasswordException("Incorrect password");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
