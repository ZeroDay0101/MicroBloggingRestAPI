package com.example.socialmediarestapi.service;

import com.example.socialmediarestapi.exception.PostNotFoundException;
import com.example.socialmediarestapi.exception.ProfileNotFoundException;
import com.example.socialmediarestapi.model.entity.Post;
import com.example.socialmediarestapi.model.entity.Profile;
import com.example.socialmediarestapi.model.entity.linkers.PostInteractor;
import com.example.socialmediarestapi.model.entity.linkers.PostLiker;
import com.example.socialmediarestapi.repository.PostRepository;
import com.example.socialmediarestapi.repository.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final JWTService jwtService;
    @PersistenceContext
    private EntityManager entityManager;


    public PostService(PostRepository postRepository, ProfileRepository profileRepository, JWTService jwtService) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.jwtService = jwtService;
    }

    public void addPost(
            Post post,
            JwtAuthenticationToken token
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        long id = jwtService.extractUserId(token.getToken().getTokenValue());
        Profile profile = profileRepository.getReferenceById(id);


        post.setPostCreator(profile);


        postRepository.saveAndFlush(post);
    }

    public void editPost(long postId, String editedText) {
        Post post = postRepository.getReferenceById(postId);

        post.setText(editedText);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Set<Post> getPostsByProfileId(long id) {
        return postRepository.getPostsByPostCreatorId(id).orElseThrow(
                () -> new ProfileNotFoundException(id)
        );
    }

    @Transactional(readOnly = true)
    public Post getPost(long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId));
    }

    @Transactional(readOnly = true)
    public Post getPostReference(long postId) {
        return postRepository.getReferenceById(postId);
    }

    public void likePost(
            long postId,
            JwtAuthenticationToken token
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        long id = jwtService.extractUserId(token.getToken().getTokenValue());


        Post post = postRepository.getReferenceById(postId);
        Profile profile = profileRepository.getReferenceById(id);

        PostInteractor postLiker = new PostLiker(post, profile);

        entityManager.persist(postLiker);
        entityManager.flush();


    }

    @Transactional
    public void unlikePost(
            long postId,
            JwtAuthenticationToken token
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        long id = jwtService.extractUserId(token.getToken().getTokenValue());


        Post post = postRepository.getReferenceById(postId);
        Profile profile = profileRepository.getReferenceById(id);


        PostLiker postLiker = new PostLiker(post, profile);
        postRepository.deletePostLiker(postLiker);
    }

    @Transactional(readOnly = true)
    public Slice<Post> getPostComments(long postId, int offset) {
        return postRepository.getPostComments(postId, PageRequest.of(offset, 15)).orElseThrow(
                () -> new PostNotFoundException(postId)
        );
    }

    @Transactional(readOnly = true)
    public Slice<Profile> getPostLikers(long postId, int offset) {
        return postRepository.getPostLikers(postId, PageRequest.of(offset, 15)).orElseThrow(
                () -> new PostNotFoundException(postId)
        );
    }
}
