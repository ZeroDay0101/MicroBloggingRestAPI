package com.example.socialmediarestapi.mappers;

import com.example.socialmediarestapi.dto.post.PostCreationDTO;
import com.example.socialmediarestapi.dto.post.PostDTO;
import com.example.socialmediarestapi.dto.post.SimplePostDTO;
import com.example.socialmediarestapi.exception.PostNotFoundException;
import com.example.socialmediarestapi.model.entity.Post;
import com.example.socialmediarestapi.repository.PostRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected ProfileMapper profileMapper;

    @Mapping(source = "referencedPostId", target = "referencedPost", qualifiedByName = "getReferencedPost")
    public abstract Post postCreationDTOToEntity(PostCreationDTO postCreationDTO);


    public abstract Set<PostDTO> postToPostDTOList(Set<Post> post);

    @Mapping(target = "profileId", expression = "java(post.getPostCreator().getId())")
    @Mapping(target = "referencedPostId", expression = "java(post.getReferencedPost() != null ? post.getReferencedPost().getId() : 0)")
//     @Mapping(target = "postLikers",expression = "java(profileMapper.profileToPostInteractor(postRepository.getPostLikers(post).get()))")
    public abstract PostDTO postToPostDTO(Post post);

    @Mapping(target = "postCreatorUsername", expression = "java(post.getPostCreator().getUsername())")
    public abstract Set<SimplePostDTO> postToSimplePostDTOList(Set<Post> post);

    @Mapping(target = "postCreatorUsername", expression = "java(post.getPostCreator().getUsername())")
    public abstract SimplePostDTO postToSimplePostDTO(Post post);


    @Named("getReferencedPost")
    public Post getReferencedPost(long referencedPostId) {
        if (referencedPostId == 0) return null;
        return postRepository.findById(referencedPostId).orElseThrow(
                () -> new PostNotFoundException(referencedPostId)
        );
    }


}
