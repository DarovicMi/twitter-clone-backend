package com.twitter.services;

import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.repositories.TweetRepository;
import com.twitter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;


    public Tweet createTweet(Tweet tweet) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();

       User loggedInUser = userRepository.findByUsername(principal.getName());
       tweet.setTweetUser_id(loggedInUser);
        tweet.setTweetCreatedAt(LocalDateTime.now());
        tweet.setTweetUpdatedAt(LocalDateTime.now());
        return tweetRepository.save(tweet);

    }

    public Tweet updateTweet(Authentication authentication, Tweet tweet) throws Exception {
        Tweet updatedTweet = tweetRepository.findById(tweet.getTweetId()).orElse(null);
        User loggedInUser = userRepository.findByUsername(authentication.getName());
        if(updatedTweet == null) {
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
        if(updatedTweet.getTweetUser_id() != loggedInUser) {
            throw new Exception("You are not authorized to edit this tweet");
        }
        updatedTweet.setTweetMessage(tweet.getTweetMessage());
        updatedTweet.setTweetUpdatedAt(LocalDateTime.now());
        return tweetRepository.save(updatedTweet);
    }

    public void deleteTweet(Authentication authentication, Long tweetId) throws Exception{
        Tweet deletedTweet = tweetRepository.findById(tweetId).orElse(null);
        if(deletedTweet == null) {
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
        User loggedInUser = userRepository.findByUsername(authentication.getName());
        if(deletedTweet.getTweetUser_id() != loggedInUser){
            throw new Exception("You are not authorized to delete this tweet");
        }
        tweetRepository.delete(deletedTweet);
    }

    public Tweet findTweet(Long tweetId) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        if(tweet == null){
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
        return tweet;
    }

}
