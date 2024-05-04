package com.example.socialmediarestapi.controller;

import com.example.socialmediarestapi.dto.post.PostCreationDTO;
import com.example.socialmediarestapi.dto.post.PostDTO;
import com.example.socialmediarestapi.dto.post.SimplePostDTO;
import com.example.socialmediarestapi.dto.profile.SimpleProfileDTO;
import com.example.socialmediarestapi.mappers.PostMapper;
import com.example.socialmediarestapi.mappers.ProfileMapper;
import com.example.socialmediarestapi.model.entity.Post;
import com.example.socialmediarestapi.security.UserDetailsImplementation;
import com.example.socialmediarestapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    private final ProfileMapper profileMapper;

    public PostController(PostService postService, PostMapper postMapper, ProfileMapper profileMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.profileMapper = profileMapper;
    }


    @PostMapping
    public ResponseEntity<Void> addPost(
            @RequestBody PostCreationDTO postCreationDTO,
            Principal principal
    ) {
        Post post = postMapper.postCreationDTOToEntity(postCreationDTO);

        postService.addPost(post, (UsernamePasswordAuthenticationToken) principal);

        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePost(@RequestParam long id) {
        Post post = postService.getPost(id);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) || ((UserDetailsImplementation) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId() == post.getPostCreator().getId()) {
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
    ) {
        postService.likePost(id, (UsernamePasswordAuthenticationToken) principal);

        return ResponseEntity.ok().body(null);
    }

    @PostMapping()
    @RequestMapping("/unlike")
    public ResponseEntity<Void> unlikePost(
            @RequestParam long id,
            Principal principal
    ) {
        postService.unlikePost(id, (UsernamePasswordAuthenticationToken) principal);

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
