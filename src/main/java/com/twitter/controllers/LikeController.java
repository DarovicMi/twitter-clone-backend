package com.twitter.controllers;


import com.twitter.entities.Liker;
import com.twitter.exceptions.CommentNotFoundException;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.services.LikerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LikeController {

    @Autowired
    private LikerService likeService;

    @PostMapping("/like/tweet/{tweetId}")
    public Liker likeTweet(@PathVariable("tweetId") Long tweetId) throws TweetNotFoundException {
        return likeService.likeTweet(tweetId);
    }
    @PostMapping("/like/comment/{commentId}")
    public Liker likeComment(@PathVariable("commentId") Long commentId) throws CommentNotFoundException {
        return likeService.likeComment(commentId);
    }
    @GetMapping("/tweet/{tweetId}/likes")
    public List<Liker> getTweetLikes(@PathVariable("tweetId") Long tweetId) throws TweetNotFoundException {
        return likeService.getTweetLikes(tweetId);
    }

    @GetMapping("/comment/{commentId}/likes")
    public List<Liker> getCommentLikes(@PathVariable("commentId") Long commentId) throws CommentNotFoundException {
        return likeService.getCommentLikes(commentId);
    }
}
