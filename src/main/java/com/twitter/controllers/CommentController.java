package com.twitter.controllers;

import com.twitter.entities.Comment;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("/tweet/{tweetId}/comment")
    public Comment createComment(@PathVariable Long tweetId, @RequestBody Comment comment) throws TweetNotFoundException {
        commentService.createTweetComment(tweetId, comment);
        return comment;
    }

    @GetMapping("/tweet/{tweetId}/comments")
    public List<Comment> getTweetComments(@PathVariable Long tweetId) throws TweetNotFoundException {
        return commentService.getTweetComments(tweetId);
    }

    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) throws Exception {
        commentService.deleteComment(commentId);
    }

    @PutMapping("/comment/{commentId}")
    public void updateComment(@PathVariable("commentId") Long commentId, @RequestBody Comment comment) throws Exception {
        commentService.updateComment(commentId, comment);
    }


}
