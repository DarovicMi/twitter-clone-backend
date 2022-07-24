package com.twitter.event;

import com.twitter.entities.User;
import com.twitter.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class TokenEventListener implements ApplicationListener<TokenEvent> {

    @Autowired
    private EmailSenderService emailService;

    @Override
    public void onApplicationEvent(TokenEvent event) {
        User user = event.getUser();
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + event.getVerificationToken().getToken();
        emailService.sendEmail(user.getEmail(), "New link for verifying registration is: \n" + url, "RESENT: Verification link");
    }
}
