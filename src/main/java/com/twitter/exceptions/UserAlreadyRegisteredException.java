package com.twitter.exceptions;

public class UserAlreadyRegisteredException extends Exception{

    public UserAlreadyRegisteredException(String msg){
        super(msg);
    }
}
