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
import com.twitter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepository userRepository;

    private User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }

    public Liker likeTweet(Long tweetId) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        User loggedInUser = getLoggedInUser();
        if(tweet == null){
            throw new TweetNotFoundException("Tweet doesn't exist");
        }

        Liker like = new Liker();
        like.setLikedTweet(tweet);
        like.setUser(loggedInUser);
        likerRepository.save(like);
        return like;
    }

    public Liker likeComment(Long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        User loggedInUser = getLoggedInUser();
        if (comment == null) {
            throw new CommentNotFoundException("Comment doesn't exist");
        }
        Liker like = new Liker();
        like.setLikedComment(comment);
        like.setUser(loggedInUser);
        likerRepository.save(like);
        return like;
    }
    public List<Liker> getTweetLikes(Long tweetId) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        if(tweet == null) {
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
        return likerRepository.getTweetLikes(tweet);
    }

    public List<Liker> getCommentLikes(Long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment == null) {
            throw new CommentNotFoundException("Comment doesn't exist");
        }
        return likerRepository.getCommentLikes(comment);
    }

}
