package com.twitter.exceptions;

public class TweetNotFoundException extends Exception{

    public TweetNotFoundException(String msg){
        super(msg);
    }
}
