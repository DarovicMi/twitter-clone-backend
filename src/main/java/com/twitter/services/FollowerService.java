package com.twitter.services;

import com.twitter.entities.Follower;
import com.twitter.entities.User;
import com.twitter.exceptions.UserNotFoundException;
import com.twitter.repositories.FollowerRepository;
import com.twitter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FollowerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowerRepository followerRepository;

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }

    public void followUser(Long userId) throws UserNotFoundException {
        User loggedInUser = getLoggedInUser();
        User followee = userRepository.findById(userId).orElse(null);
        if(followee == null){
            throw new UserNotFoundException(String.format("User with id = %d doesn't exist", userId));
        }
        Follower follower = new Follower();
        follower.setFollower(loggedInUser);
        follower.setFollowee(followee);
        followerRepository.save(follower);
    }

    public void unfollowUser(Long userId) throws Exception {
        User loggedInUser = getLoggedInUser();
        User following = userRepository.findById(userId).orElse(null);
        if(following == null){
            throw new UserNotFoundException(String.format("User with id = %d doesn't exist", userId));
        }
        Follower follower = followerRepository.findByFolloweeAndFollower(following,loggedInUser).orElse(null);
        if(follower == null){
            throw new Exception("Follower not found");
        }
        followerRepository.delete(follower);
    }

    public List<Follower> getUserFollowers(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        return followerRepository.findUserFollowers(user);
    }

    public List<Follower> getUserFollowee(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return followerRepository.findUserFollowee(user);
    }

}
