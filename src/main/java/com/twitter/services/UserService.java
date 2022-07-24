package com.twitter.services;

import com.twitter.entities.User;
import com.twitter.entities.UserAccountType;
import com.twitter.entities.UserStatus;
import com.twitter.exceptions.*;
import com.twitter.repositories.PasswordResetTokenRepository;
import com.twitter.repositories.UserRepository;
import com.twitter.repositories.VerificationTokenRepository;
import com.twitter.verificationenums.PasswordResetToken;
import com.twitter.verificationenums.TokenStatusEnum;
import com.twitter.verificationenums.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    public User authenticate() throws Exception {
        User loggedInUser = userAuthenticationService.getLoggedInUser();
       User email = userRepository.findByEmail(loggedInUser.getEmail());
       if(email == null){
           throw new Exception("Email not found");
       }
       User password = userRepository.findByPassword(loggedInUser.getPassword());
       if(password == null){
           throw new Exception("Password not found");
       }
        return loggedInUser;
    }

    public Boolean userExists(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return true;
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return true;
        }
        return false;
    }

    public void registerUser(User user) throws Exception {
        if (userExists(user)) {
            throw new UserAlreadyRegisteredException("User already registered");
        }

        String userEmail = user.getEmail();
        String userUserName = user.getUsername();
        String userPassword = user.getPassword();
        UserAccountType userAccountType = user.getAccountType();
        String imageUrl = user.getImageUrl();


        boolean invalidUsername = (userUserName == null);
        boolean invalidEmail = (userEmail == null);
        boolean invalidPassword = (userPassword == null);

        if (invalidPassword) throw new InvalidPasswordException("Incorrect password");
        if (invalidUsername) throw new InvalidUsernameException("Incorrect username");
        if (invalidEmail) throw new InvalidEmailException("Incorrect email");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreationDate(LocalDateTime.now());
        user.setAccountType(userAccountType);
        user.setImageUrl(imageUrl);
        user.setAccountStatus(UserStatus.INACTIVE);

        userRepository.save(user);

    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public void updateUser(Long userId, User user) throws UserNotFoundException {
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id = %s was not found", userId)));
        updatedUser.setEmail(user.getEmail());
        updatedUser.setAccountType(user.getAccountType());
        updatedUser.setImageUrl(user.getImageUrl());
        updatedUser.setUsername(user.getUsername());

        userRepository.save(updatedUser);
    }

    public void deleteUser(Long userId) throws UserNotFoundException {
        boolean deletedUser = userRepository.existsById(userId);
        if (!deletedUser) {
            throw new UserNotFoundException(String.format("User with id = %s was not found", userId));
        }
        userRepository.deleteById(userId);
    }

    // VERIFICATION OF USER

    public TokenStatusEnum validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TokenStatusEnum.INVALID_TOKEN;
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationDate().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationToken.setTokenStatus(TokenStatusEnum.EXPIRED_TOKEN);
            return TokenStatusEnum.EXPIRED_TOKEN;
        }
        user.setAccountStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return TokenStatusEnum.VALID_TOKEN;

    }

    public VerificationToken getUserOldToken(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        return verificationTokenRepository.findVerificationTokenByUserId(user.getId());
    }

    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setTokenStatus(TokenStatusEnum.VALID_TOKEN);
        verificationToken.setExpirationDate(verificationToken.calculateExpirationDate(10));
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    public PasswordResetToken createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }

    public PasswordResetToken validateOrGeneratePasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        if (user != null) {
            PasswordResetToken passwordToken = createPasswordResetTokenForUser(user, token);
            TokenStatusEnum passwordResetTokenStatus = validatePasswordResetToken(passwordToken.getToken());
            if (passwordResetTokenStatus.equals(TokenStatusEnum.VALID_TOKEN)) {
                return passwordToken;
            }
        }
        token = UUID.randomUUID().toString();
        return createPasswordResetTokenForUser(user, token);
    }

    public TokenStatusEnum validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            return TokenStatusEnum.INVALID_TOKEN;
        }
        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if ((passwordResetToken.getExpirationDate().getTime() - calendar.getTime().getTime() <= 0)) {
            passwordResetToken.setTokenStatus(TokenStatusEnum.EXPIRED_TOKEN);
            return TokenStatusEnum.EXPIRED_TOKEN;
        }
        userRepository.save(user);
        return TokenStatusEnum.VALID_TOKEN;
    }

    public User getUserByPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token).getUser();
    }


    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    public void changePassword(User user, String newPassword) throws InvalidPasswordException {
        boolean invalidPassword = (user.getPassword() == null);
        if (invalidPassword) throw new InvalidPasswordException("Incorrect password");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
