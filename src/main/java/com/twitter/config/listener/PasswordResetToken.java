package com.twitter.config.listener;

import com.twitter.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private User user;

    public PasswordResetToken(User user, String token) {
        this.token = token;
        this.user = user;
        this.expirationDate = calculateExpirationDate(EXPIRATION_TIME);
    }

    public PasswordResetToken(String token){
        this.token = token;
        this.expirationDate = calculateExpirationDate(EXPIRATION_TIME);
    }

    private Date calculateExpirationDate(int time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, time);
        return new Date(calendar.getTime().getTime());
    }

}
