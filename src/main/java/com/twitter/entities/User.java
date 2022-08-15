package com.twitter.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username field cannot be empty")
    private String username;

    @NotNull(message = "Password field cannot be empty")
    private String password;

    @NotNull(message = "Email field cannot be empty")
    private String email;

    private LocalDateTime creationDate;

    @Enumerated(value = EnumType.STRING)
    private UserStatus accountStatus;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private UserAccountType accountType;


    @OneToMany(mappedBy = "follower")
    @JsonIgnore
    private List<Follower> follower;



    public List<Follower> getFollows() {
        return follower;
    }

    public void setFollows(List<Follower> follows) {
        this.follower = follows;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public UserStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(UserStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(UserAccountType accountType) {
        this.accountType = accountType;
    }

}
