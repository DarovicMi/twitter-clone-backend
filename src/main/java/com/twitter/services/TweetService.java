package com.twitter.services;

import com.twitter.entities.Follower;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserAuthenticationService userAuthenticationService;


    public List<User> shareTweet(Long tweetId) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException("Tweet doesn't exist"));
        User loggedInUser = userAuthenticationService.getLoggedInUser();
        tweet.addSharedBy(loggedInUser);
        tweet = tweetRepository.save(tweet);
        return tweet.getSharedBy();
    }


    public List<Tweet> showFeed() {
      User loggedInUser = userAuthenticationService.getLoggedInUser();
      List<Follower> usersFollowedByLoggedInUser = followerService.getUserFollowee(loggedInUser.getId());
      List<Tweet> userTweets = new ArrayList<>();
      for(Follower f : usersFollowedByLoggedInUser) {
          userTweets.addAll(tweetRepository.findLatestUserTweet(f.getId()));
      }
      return userTweets;
    }

    public void createTweet(Tweet tweet) {
        User loggedInUser = userAuthenticationService.getLoggedInUser();
        tweet.setUser(loggedInUser);
        tweet.setCreatedAt(LocalDateTime.now());
        tweet.setUpdatedAt(LocalDateTime.now());
        tweetRepository.save(tweet);

    }

    public void updateTweet(Long tweetId, Tweet tweet) throws Exception {
        Tweet updatedTweet = tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException("Tweet doesn't exist"));
        User loggedInUser = userAuthenticationService.getLoggedInUser();
        if (updatedTweet.getUser() != loggedInUser) {
            throw new Exception("You are not authorized to edit this tweet");
        }
        updatedTweet.setMessage(tweet.getMessage());
        updatedTweet.setUpdatedAt(LocalDateTime.now());
        tweetRepository.save(updatedTweet);
    }

    public void deleteTweet(Long tweetId) throws Exception {
        Tweet deletedTweet = tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException("Tweet doesn't exist"));
        User loggedInUser = userAuthenticationService.getLoggedInUser();

        if (deletedTweet.getUser() != loggedInUser) {
            throw new Exception("You are not authorized to delete this tweet");
        }
        tweetRepository.delete(deletedTweet);
    }

    public Tweet findTweet(Long tweetId) throws TweetNotFoundException {
        return tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException("Tweet doesn't exist"));
    }

    public List<Tweet> getTweets() {
        return tweetRepository.findAll();
    }


    public List<Tweet> showUserTweets(Long userId){
        return tweetRepository.findLatestUserTweet(userId);
    }
}
