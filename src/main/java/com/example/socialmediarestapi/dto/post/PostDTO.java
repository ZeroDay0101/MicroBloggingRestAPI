package com.example.socialmediarestapi.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDTO {
    private long id;
    private String text;
    private long profileId;
    private long referencedPostId;

}
