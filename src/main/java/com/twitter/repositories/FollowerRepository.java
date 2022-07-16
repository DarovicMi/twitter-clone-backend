package com.twitter.repositories;

import com.twitter.entities.Follower;
import com.twitter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    @Query(value = "SELECT f FROM Follower f WHERE f.followee =?1 AND f.follower=?2")
    Optional<Follower> findByFolloweeAndFollower(User followee, User follower);

    @Query(value = "SELECT f from Follower f WHERE f.follower = ?1")
    List<Follower> findUserFollowers(User loggedInUser);

    @Query(value = "SELECT f FROM Follower f WHERE f.followee = ?1")
    List<Follower> findUserFollowee(User loggedInUser);
}
