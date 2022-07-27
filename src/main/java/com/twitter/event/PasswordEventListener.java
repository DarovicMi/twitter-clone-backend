package com.twitter.event;

import com.twitter.entities.User;
import com.twitter.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class PasswordEventListener implements ApplicationListener<PasswordEvent> {

    @Autowired
    private EmailSenderService emailService;

    @Override
    public void onApplicationEvent(PasswordEvent event) {
        User user = event.getUser();
        String url = "http://localhost:4200" + "/savePassword";
        emailService.sendEmail(user.getEmail(),"Click the following link to reset your password: \n" + url, "Reset Password");

    }
}
