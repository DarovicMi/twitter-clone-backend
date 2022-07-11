package com.twitter.controllers;


import com.twitter.entities.Tweet;
import com.twitter.services.TweetService;
import com.twitter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TweetController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TweetService tweetService;
    
    @PostMapping("/tweet")
    public Tweet createTweet(@RequestBody Tweet tweet){
        tweetService.createTweet(tweet);
        return tweet;
    }

    //public Tweet updateTweet(Authentication auth, @RequestBody Tweet tweet, Long id)
    
}
