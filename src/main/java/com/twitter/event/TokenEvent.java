package com.twitter.event;

import com.twitter.entities.User;
import com.twitter.verificationenums.VerificationToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class TokenEvent extends ApplicationEvent {

    private User user;
    private VerificationToken verificationToken;
    private String applicationUrl;

    public TokenEvent(User user, VerificationToken verificationToken, String applicationUrl) {
        super(verificationToken);
        this.verificationToken = verificationToken;
        this.applicationUrl = applicationUrl;
        this.user = user;
    }
}
