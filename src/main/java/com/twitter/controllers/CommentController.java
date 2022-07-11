package com.twitter.controllers;

import com.twitter.entities.Comment;
import com.twitter.entities.Tweet;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.services.CommentService;
import com.twitter.services.TweetService;
import com.twitter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TweetService tweetService;

    @PostMapping("/comment")
    public String createComment(Long tweetId,Authentication auth, @RequestBody Comment comment) throws TweetNotFoundException {
      Tweet findTweet = tweetService.findTweet(tweetId);
     commentService.createTweetComment(findTweet.getTweetId(),auth, comment);
     return "Comment successfully created";
  }

}
