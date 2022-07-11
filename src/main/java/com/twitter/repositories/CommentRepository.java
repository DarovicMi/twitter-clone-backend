package com.twitter.repositories;

import com.twitter.entities.Comment;
import com.twitter.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByTweetId(Tweet tweet);
}
