package com.twitter.controllers;

import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TweetController {

    
    @Autowired
    private TweetService tweetService;

    
    @PostMapping("/tweet")
    public Tweet createTweet(@RequestBody Tweet tweet){
       tweetService.createTweet(tweet);
       return tweet;
    }

    @PostMapping("/share/tweet/{tweetId}")
    public List<User> shareTweet(@PathVariable("tweetId") Long tweetId) throws TweetNotFoundException {
        return tweetService.shareTweet(tweetId);
    }

    @GetMapping("/tweets/feed")
    public List<Tweet> showFeed() {
        return tweetService.showFeed();
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
    @GetMapping("/user/{userId}/tweets")
    public List<Tweet> showUserTweets(@PathVariable("userId") Long userId){
        return tweetService.showUserTweets(userId);
    }

    
}
