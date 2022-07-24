package com.twitter.verificationenums;

import com.twitter.entities.User;


import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class VerificationToken {

    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationDate;

    public VerificationToken() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TokenStatusEnum getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(TokenStatusEnum tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    @Enumerated(value = EnumType.STRING)
    private TokenStatusEnum tokenStatus;

    public VerificationToken(User user, String token) {
        this.token = token;
        this.user = user;
        this.expirationDate = calculateExpirationDate(EXPIRATION_TIME);
    }

    public VerificationToken(String token){
        this.token = token;
        this.expirationDate = calculateExpirationDate(EXPIRATION_TIME);
    }

    public Date calculateExpirationDate(int time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, time);
        return new Date(calendar.getTime().getTime());
    }
}
