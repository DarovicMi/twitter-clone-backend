package com.twitter.controllers;


import com.twitter.entities.Tweet;
import com.twitter.model.LikerModel;
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
import java.util.stream.Collectors;

@RestController
public class LikerController {

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
    @PostMapping("/unlike/tweet/{tweetId}")
    public void unlikeTweet(@PathVariable("tweetId") Long tweetId) throws TweetNotFoundException {
        likeService.unlikeTweet(tweetId);
    }
    @PostMapping("/unlike/comment/{commentId}")
    public void unlikeComment(@PathVariable("commentId") Long commentId) throws CommentNotFoundException {
        likeService.unlikeComment(commentId);
    }
    @GetMapping("/tweet/{tweetId}/likes")
    public List<LikerModel> getTweetLikes(@PathVariable("tweetId") Long tweetId) throws TweetNotFoundException {
        return likeService.getTweetLikes(tweetId)
                .stream()
                .map(LikerModel::mapLikerToModel).collect(Collectors.toList());
    }

    @GetMapping("/comment/{commentId}/likes")
    public List<LikerModel> getCommentLikes(@PathVariable("commentId") Long commentId) throws CommentNotFoundException {
        return likeService.getCommentLikes(commentId)
                .stream()
                .map(LikerModel::mapLikerToModel).collect(Collectors.toList());
    }
}
