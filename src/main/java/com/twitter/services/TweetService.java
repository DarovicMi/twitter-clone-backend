package com.twitter.services;

import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.repositories.CommentRepository;
import com.twitter.repositories.TweetRepository;
import com.twitter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;


    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }

    public List<Tweet> showFeed(User user) {
        Tweet lastTweet = null;
        List<Tweet> userTweet = tweetRepository.findLatestUserTweet(user.getId());

        if (userTweet.size() > 0) {
            lastTweet = userTweet.get(0);
        }
        List<Tweet> followedTweets = tweetRepository.findUserTweets(user);
        if (lastTweet != null) {
            followedTweets.add(0, lastTweet);
        }

        return followedTweets;
    }

    public void createTweet(Tweet tweet) {
        User loggedInUser = getLoggedInUser();
        tweet.setUser(loggedInUser);
        tweet.setCreatedAt(LocalDateTime.now());
        tweet.setUpdatedAt(LocalDateTime.now());
        tweetRepository.save(tweet);

    }

    public void updateTweet(Long tweetId, Tweet tweet) throws Exception {
        Tweet updatedTweet = tweetRepository.findById(tweetId).orElse(null);
        User loggedInUser = getLoggedInUser();
        if (updatedTweet == null) {
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
        if (updatedTweet.getUser() != loggedInUser) {
            throw new Exception("You are not authorized to edit this tweet");
        }
        updatedTweet.setMessage(tweet.getMessage());
        updatedTweet.setUpdatedAt(LocalDateTime.now());
        tweetRepository.save(updatedTweet);
    }

    public void deleteTweet(Long tweetId) throws Exception {

        Tweet deletedTweet = tweetRepository.findById(tweetId).orElse(null);
        if (deletedTweet == null) {
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
        User loggedInUser = getLoggedInUser();
        if (deletedTweet.getUser() != loggedInUser) {
            throw new Exception("You are not authorized to delete this tweet");
        }
        tweetRepository.delete(deletedTweet);
    }

    public Tweet findTweet(Long tweetId) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        if (tweet == null) {
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
        return tweet;
    }

    public List<Tweet> getTweets() {
        return tweetRepository.findAll();
    }


    public List<Tweet> showUserTweets(Long userId){
        return tweetRepository.findLatestUserTweet(userId);
    }
}
