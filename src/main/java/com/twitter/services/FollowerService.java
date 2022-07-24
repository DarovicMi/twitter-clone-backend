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

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    public void followUser(Long userId) throws UserNotFoundException {
        User loggedInUser = userAuthenticationService.getLoggedInUser();
        User followee = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id = %d doesn't exist", userId)));
        Follower follower = new Follower();
        follower.setFollower(loggedInUser);
        follower.setFollowee(followee);
        followerRepository.save(follower);
    }

    public void unfollowUser(Long userId) throws Exception {
        User loggedInUser = userAuthenticationService.getLoggedInUser();
        User following = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id = %d doesn't exist", userId)));
        Follower follower = followerRepository.findByFolloweeAndFollower(following,loggedInUser)
                .orElseThrow(() -> new Exception("Follower not found"));
        followerRepository.delete(follower);
    }

    public List<Follower> getUserFollowers(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with id = %d was not found",userId)));
        return followerRepository.findUserFollowers(user);
    }

    public List<Follower> getUserFollowee(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return followerRepository.findUserFollowee(user);
    }

}
