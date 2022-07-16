package com.twitter.controllers;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.repositories.UserRepository;
import com.twitter.services.TweetService;
import com.twitter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TweetController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/tweet")
    public Tweet createTweet(@RequestBody Tweet tweet){
       tweetService.createTweet(tweet);
       return tweet;
    }

    @GetMapping("/tweets/feed")
    public List<Tweet> showFeed(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByUsername(authentication.getName());
        return tweetService.showFeed(loggedInUser);
    }

    @GetMapping("/tweets")
    public List<Tweet> getTweets(){
        return tweetService.getTweets();
    }

    @PutMapping("/tweet/{tweetId}")
    public void updateTweet(@PathVariable("tweetId") Long tweetId, @RequestBody Tweet tweet) throws Exception {
        tweetService.updateTweet(tweetId,tweet);
    }
    @DeleteMapping("/tweet/{tweetId}")
    public void deleteTweet(@PathVariable("tweetId") Long tweetId) throws Exception {
        tweetService.deleteTweet(tweetId);
    }
    @GetMapping("/tweet/{tweetId}")
    public Tweet findTweet(@PathVariable Long tweetId) throws TweetNotFoundException {
       return tweetService.findTweet(tweetId);
    }
    @GetMapping("/user/tweet/{tweetId}")
    public List<Tweet> showUserTweets(@PathVariable("tweetId") Long tweetId){
        return tweetService.showUserTweets(tweetId);
    }

    
}
