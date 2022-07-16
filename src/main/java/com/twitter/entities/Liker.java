package com.twitter.entities;

import javax.persistence.*;

@Entity
public class Liker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Tweet likedTweet;

    @OneToOne
    private Comment likedComment;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tweet getLikedTweet() {
        return likedTweet;
    }

    public void setLikedTweet(Tweet likedTweet) {
        this.likedTweet = likedTweet;
    }

    public Comment getLikedComment() {
        return likedComment;
    }

    public void setLikedComment(Comment likedComment) {
        this.likedComment = likedComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
