package com.example.socialmediarestapi.repository;

import com.example.socialmediarestapi.model.entity.Post;
import com.example.socialmediarestapi.model.entity.Profile;
import com.example.socialmediarestapi.model.entity.linkers.PostLiker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Set<Post>> getPostsByPostCreatorId(long id);

    @Query("SELECT p FROM Profile p JOIN FETCH liked_posts lp on lp.post.id = :postId where p.id = lp.profile.id")
    Optional<Slice<Profile>> getPostLikers(long postId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM liked_posts lp WHERE lp = :postLiker")
    void deletePostLiker(PostLiker postLiker);

    @Query("SELECT p FROM posts p where p.referencedPost.id = :postId")
    Optional<Page<Post>> getPostComments(long postId, Pageable pageable);

}
