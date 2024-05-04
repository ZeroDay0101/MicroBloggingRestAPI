package com.example.socialmediarestapi.model.entity;

import com.example.socialmediarestapi.model.entity.linkers.PostLiker;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity(name = "posts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("Post")
@Builder
@Setter
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id")
    private Profile postCreator;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn
    private Post referencedPost;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<PostLiker> postLikers;

    @OneToMany(mappedBy = "referencedPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Post> postComments;

}
