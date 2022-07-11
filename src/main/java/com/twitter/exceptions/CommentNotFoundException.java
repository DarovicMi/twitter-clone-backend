package com.twitter.exceptions;

public class CommentNotFoundException extends Exception{
    public CommentNotFoundException(String msg){
        super(msg);
    }
}
