package com.twitter.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tweetId;

    @NotNull(message = "Tweet message cannot be empty")
    @Size(max = 255)
    private String tweetMessage;

    private LocalDateTime tweetCreatedAt;

    private LocalDateTime tweetUpdatedAt;

    @ManyToOne
    private User tweetUser;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "commentTweetId")
    List<Comment> comments;
}