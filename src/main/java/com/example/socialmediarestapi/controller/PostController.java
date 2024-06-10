package com.example.socialmediarestapi.controller;

import com.example.socialmediarestapi.dto.post.PostCreationDTO;
import com.example.socialmediarestapi.dto.post.PostDTO;
import com.example.socialmediarestapi.dto.post.SimplePostDTO;
import com.example.socialmediarestapi.dto.profile.SimpleProfileDTO;
import com.example.socialmediarestapi.mappers.PostMapper;
import com.example.socialmediarestapi.mappers.ProfileMapper;
import com.example.socialmediarestapi.model.entity.Post;
import com.example.socialmediarestapi.security.authentication.UserDetailsImplementation;
import com.example.socialmediarestapi.service.JWTService;
import com.example.socialmediarestapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

@RestController
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    private final ProfileMapper profileMapper;

    private final JWTService jwtService;

    public PostController(PostService postService, PostMapper postMapper, ProfileMapper profileMapper, JWTService jwtService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.profileMapper = profileMapper;
        this.jwtService = jwtService;
    }


    @PostMapping
    public ResponseEntity<Void> addPost(
            @RequestBody PostCreationDTO postCreationDTO,
            Principal principal
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Post post = postMapper.postCreationDTOToEntity(postCreationDTO);

        postService.addPost(post, (JwtAuthenticationToken) principal);

        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePost(
            @RequestParam long id,
            Authentication authentication
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        Post post = postService.getPost(id);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) || jwtService.extractUserId(((JwtAuthenticationToken) authentication).getToken().getTokenValue()) == post.getPostCreator().getId()) {
            postService.deletePost(postService.getPost(id));
            return ResponseEntity.ok().body(null);
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @GetMapping
    public ResponseEntity<PostDTO> getPostById(@RequestParam long id) {
        PostDTO post = postMapper.postToPostDTO(postService.getPost(id));

        return ResponseEntity.ok(post);
    }

    @GetMapping()
    @RequestMapping("/byProfile")
    public ResponseEntity<Set<PostDTO>> getPostsByProfileId(@RequestParam long id) {
        Set<PostDTO> posts = postMapper.postToPostDTOList(
                postService.getPostsByProfileId(id)
        );

        return ResponseEntity.ok(posts);
    }


    @PostMapping()
    @RequestMapping("/like")
    public ResponseEntity<Void> likePost(
            @RequestParam long id,
            Principal principal
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        postService.likePost(id, (JwtAuthenticationToken) principal);

        return ResponseEntity.ok().body(null);
    }

    @PostMapping()
    @RequestMapping("/unlike")
    public ResponseEntity<Void> unlikePost(
            @RequestParam long id,
            Principal principal
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        postService.unlikePost(id, (JwtAuthenticationToken) principal);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    @RequestMapping("/comments/{id}/")
    public ResponseEntity<Set<SimplePostDTO>> getPostComment(
            @PathVariable long id,
            @RequestParam int offset
    ) {
        Set<SimplePostDTO> postDTOs = postMapper.postToSimplePostDTOList(postService.getPostComments(id, offset).toSet());
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping
    @RequestMapping("/likes/{id}/")
    public ResponseEntity<Set<SimpleProfileDTO>> getPostLikers(
            @PathVariable long id,
            @RequestParam int offset
    ) {
        Set<SimpleProfileDTO> profileDTOs = profileMapper.profileToSimpleProfileDTOList(postService.getPostLikers(id, offset)).toSet();
        return ResponseEntity.ok(profileDTOs);
    }

}
