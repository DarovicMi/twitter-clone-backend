package com.twitter.entities;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull(message = "Username field cannot be empty")
    @Size(min = 8, max = 16)
    private String username;

    @NotNull(message = "Password field cannot be empty")
    @Size(min = 8, max = 16)
    private String password;

    @NotNull(message = "Email field cannot be empty")
    @Size(max = 255)
    private String email;

    private Date creationDate;

    @Enumerated(value = EnumType.STRING)
    private UserStatus USER_STATUS;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private UserAccountType USER_ACCOUNT_TYPE;

    @OneToMany
    private List<User> following;

    @OneToMany
    private List<User> followers;



}
