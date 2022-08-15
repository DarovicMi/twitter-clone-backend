package com.twitter.repositories;

import com.twitter.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query(value = "SELECT t FROM Tweet t WHERE t.user.id = ?1 ORDER BY created_at DESC")
    List<Tweet> findLatestUserTweet(Long userId);

    @Query(value = "SELECT t FROM Tweet t ORDER BY created_at DESC")
    List<Tweet> findAll();


}
