package com.twitter.controllers;


import com.twitter.entities.Follower;
import com.twitter.exceptions.UserNotFoundException;
import com.twitter.services.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @PostMapping("/follow/user/{userId}")
    public void followUser(@PathVariable("userId") Long userId) throws UserNotFoundException {
        followerService.followUser(userId);
    }
    @PostMapping("/unfollow/user/{userId}")
    public void unfollowUser(@PathVariable("userId") Long userId) throws Exception {
        followerService.unfollowUser(userId);
    }

    @GetMapping("/user/{userId}/followers")
    public List<Follower> getUserFollowers(@PathVariable("userId")Long userId){
        return followerService.getUserFollowers(userId);
    }

    @GetMapping("/user/{userId}/followee")
    public List<Follower> getUserFollowee(@PathVariable("userId") Long userId) {
        return followerService.getUserFollowee(userId);
    }
}
