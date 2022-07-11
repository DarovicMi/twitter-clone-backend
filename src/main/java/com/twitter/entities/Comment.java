package com.twitter.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String commentText;

    @ManyToOne
    private User commentUserId;

    @ManyToOne
    private Tweet tweetId;

    private LocalDateTime commentCreatedAt;

    private LocalDateTime commentUpdatedAt;

}