package com.twitter.repositories;

import com.twitter.entities.Comment;
import com.twitter.entities.Liker;
import com.twitter.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikerRepository extends JpaRepository<Liker, Long> {
    @Query(value = "SELECT l from Liker l WHERE l.likedTweet = ?1")
    List<Liker> getTweetLikes(Tweet tweet);

    @Query(value = "SELECT l FROM Liker l WHERE l.likedComment = ?1")
    List<Liker> getCommentLikes(Comment comment);
}
