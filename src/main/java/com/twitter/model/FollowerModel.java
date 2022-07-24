package com.twitter.model;

import com.twitter.entities.Follower;



public class FollowerModel {

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

    public static FollowerModel mapFollowerToModel(Follower follower) {
        FollowerModel followerModel = new FollowerModel();
        followerModel.setUsername(follower.getFollower().getUsername());
        followerModel.setAvatar(follower.getFollower().getImageUrl());
        return followerModel;
    }
}
