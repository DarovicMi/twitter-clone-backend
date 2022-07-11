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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment createTweetComment(Long tweetId, Authentication authentication, Comment comment) throws TweetNotFoundException {
        // TODO: load user from spring context
        //comment.setCommentUserId();
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        User loggedInUser = userRepository.findByUsername(authentication.getName());
        if (tweet.isPresent()) {
            comment.setTweetId(tweet.get());
            comment.setCommentUserId(loggedInUser);
            comment.setCommentCreatedAt(LocalDateTime.now());
            comment.setCommentUpdatedAt(LocalDateTime.now());
        } else {
            throw new TweetNotFoundException("Tweet doesn't exist");
        }
        return commentRepository.save(comment);
    }

    public void deleteComment(Authentication authentication, Long commentId) throws Exception {

        Comment deletedComment = commentRepository.findById(commentId).orElse(null);
        if (deletedComment == null) {
            throw new CommentNotFoundException("Comment doesn't exist");
        }
        User loggedInUser = userRepository.findByUsername(authentication.getName());

        if (deletedComment.getCommentUserId() != loggedInUser) {
            throw new Exception("Not authorized to delete this comment");
        }
        commentRepository.delete(deletedComment);

    }

    public Comment updateComment(Authentication authentication, Comment comment) throws Exception {
        Comment updatedComment = commentRepository.findById(comment.getCommentId()).orElse(null);
        if(updatedComment == null) {
            throw new CommentNotFoundException("Comment doesn't exist");
        }
        User loggedInUser = userRepository.findByUsername(authentication.getName());

        if(updatedComment.getCommentUserId() != loggedInUser){
            throw new Exception("Not authorized to edit this comment");
        }
        updatedComment.setCommentText(comment.getCommentText());
        updatedComment.setCommentUpdatedAt(LocalDateTime.now());

        commentRepository.save(updatedComment);

        return updatedComment;
    }

    public List<Comment> showComments(Long tweet_id){
        Tweet tweet = tweetRepository.findById(tweet_id).orElse(null);
        return commentRepository.findByTweetId(tweet);
    }

}
