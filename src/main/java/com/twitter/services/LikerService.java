package com.twitter.services;

import com.twitter.entities.Comment;
import com.twitter.entities.Liker;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.CommentNotFoundException;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.repositories.CommentRepository;
import com.twitter.repositories.LikerRepository;
import com.twitter.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LikerService {

    @Autowired
    private LikerRepository likerRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserAuthenticationService userAuthenticationService;



    public Liker likeTweet(Long tweetId) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException("Tweet doesn't exist"));
        User loggedInUser = userAuthenticationService.getLoggedInUser();

        Liker like = new Liker();
        like.setLikedTweet(tweet);
        like.setUser(loggedInUser);
        tweet.ifTweetIsLiked();
        likerRepository.save(like);
        return like;
    }

    public Liker likeComment(Long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment doesn't exist"));
        User loggedInUser = userAuthenticationService.getLoggedInUser();
        Liker like = new Liker();
        like.setLikedComment(comment);
        like.setUser(loggedInUser);
        likerRepository.save(like);
        return like;
    }

    public void unlikeTweet(Long tweetId) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException("Tweet doesn't exist"));
        User loggedInUser = userAuthenticationService.getLoggedInUser();
        Liker like = likerRepository.findByUser(loggedInUser);
        tweet.ifTweetIsUnliked();
        likerRepository.delete(like);
    }

    public void unlikeComment(Long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Tweet doesn't exist"));
        User loggedInUser = userAuthenticationService.getLoggedInUser();
        Liker unlike = likerRepository.findByLikedCommentAndUser(comment, loggedInUser);
        likerRepository.delete(unlike);
    }

    public List<Liker> getTweetLikes(Long tweetId) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException("Tweet doesn't exist"));

        return likerRepository.getTweetLikes(tweet);
    }

    public List<Liker> getCommentLikes(Long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment doesn't exist") );
        return likerRepository.getCommentLikes(comment);
    }

}
