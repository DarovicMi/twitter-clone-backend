package com.twitter.event;

import com.twitter.entities.User;
import com.twitter.verificationenums.PasswordResetToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PasswordEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;
    private PasswordResetToken passwordResetToken;

    public PasswordEvent(User user, String applicationUrl, PasswordResetToken passwordResetToken) {
        super(passwordResetToken);
        this.user = user;
        this.applicationUrl = applicationUrl;
        this.passwordResetToken = passwordResetToken;
    }
}
