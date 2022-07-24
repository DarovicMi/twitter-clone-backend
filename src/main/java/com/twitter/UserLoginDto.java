package com.twitter;

public class UserLoginDto {
    private String username;
    private String password;
    private String accountStatus;

    public UserLoginDto() {
    }

    public UserLoginDto(String username, String password, String accountStatus) {
        this.username = username;
        this.password = password;
        this.accountStatus = accountStatus;
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

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
