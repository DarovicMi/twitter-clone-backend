package com.twitter.exceptions;

public class InvalidUsernameException extends Exception{
    public InvalidUsernameException(String msg){
        super(msg);
    }
}
