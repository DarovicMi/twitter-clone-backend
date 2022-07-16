package com.twitter.repositories;

import com.twitter.entities.Tweet;
import com.twitter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query(value = "SELECT t FROM Tweet t WHERE t.user.id = ?1 ORDER BY updated_at DESC")
    List<Tweet> findLatestUserTweet(Long userId);

    @Query(value = "SELECT * FROM tweet WHERE user_id IN (SELECT followee FROM follower WHERE follower = ?1) ORDER BY updated_at DESC ", nativeQuery = true)
    List<Tweet> findUserTweets(User user);

}
