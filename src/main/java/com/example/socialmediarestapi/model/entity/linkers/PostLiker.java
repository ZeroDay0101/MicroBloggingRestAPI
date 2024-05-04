package com.example.socialmediarestapi.model.entity.linkers;

import com.example.socialmediarestapi.model.entity.Post;
import com.example.socialmediarestapi.model.entity.Profile;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity(name = "liked_posts")
@NoArgsConstructor
public class PostLiker extends PostInteractor {
    public PostLiker(Post post, Profile profile) {
        super(post, profile);
    }
}
