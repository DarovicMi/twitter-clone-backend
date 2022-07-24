package com.twitter.model;

import com.twitter.entities.Follower;

public class FolloweeModel {

    private String username;
    private String avatar;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static FolloweeModel mapFolloweeToModel(Follower followee){
        FolloweeModel followeeModel = new FolloweeModel();
        followeeModel.setUsername(followee.getFollowee().getUsername());
        followeeModel.setAvatar(followee.getFollowee().getImageUrl());
        return followeeModel;
    }
}
