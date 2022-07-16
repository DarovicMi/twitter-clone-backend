package com.twitter.services;

import com.twitter.entities.Comment;
import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import com.twitter.exceptions.CommentNotFoundException;
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
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;


    private User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }

    public Comment createTweetComment(Long tweetId, Comment comment) throws TweetNotFoundException {
        Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        User loggedInUser = getLoggedInUser();
        if (tweet != null) {
            comment.setTweet(tweet);
            comment.setUser(loggedInUser);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdatedAt(LocalDateTime.now());
        } else {
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
       return commentRepository.save(comment);
    }

    public List<Comment> getTweetComments(Long tweetId) {
      Tweet tweet = tweetRepository.findById(tweetId).orElse(null);
        return commentRepository.findByTweetId(tweet);
    }

    public void deleteComment(Long commentId) throws Exception {
        Comment deletedComment = commentRepository.findById(commentId).orElse(null);
        if (deletedComment == null) {
            throw new CommentNotFoundException("Comment doesn't exist");
        }
        User loggedInUser = getLoggedInUser();

        if (deletedComment.getUser() != loggedInUser) {
            throw new Exception("Not authorized to delete this comment");
        }
        commentRepository.delete(deletedComment);

    }

    public void updateComment(Long commentId, Comment comment) throws Exception {
        Comment updatedComment = commentRepository.findById(commentId).orElse(null);
        if (updatedComment == null) {
            throw new CommentNotFoundException("Comment doesn't exist");
        }
        User loggedInUser = getLoggedInUser();

        if (updatedComment.getUser() != loggedInUser) {
            throw new Exception("Not authorized to edit this comment");
        }

        updatedComment.setText(comment.getText());
        updatedComment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(updatedComment);
    }


}
