package com.twitter.model;

import com.twitter.entities.Liker;

public class LikerModel {

    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static LikerModel mapLikerToModel(Liker liker){
        LikerModel likeModel = new LikerModel();
        likeModel.setUsername(liker.getUser().getUsername());
        return likeModel;
    }
}
