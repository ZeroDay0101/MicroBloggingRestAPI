package com.example.socialmediarestapi.model.entity;


import com.example.socialmediarestapi.model.entity.linkers.PostLiker;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

/**
 * Profile entity is a User related table that holds "user-friendly" information about this User. This object technically CAN be returned to a presentation layer to be viewed by a user.
 * However, because of lose-coupling DTO is returned instead even tho it looks totally the same.
 */

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Profile {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn()
    @MapsId
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    @CreatedDate
    private LocalDate creationDate;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostLiker> likedPosts;

    @OneToMany(mappedBy = "postCreator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "postCreator", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> commentedPosts;
}
