package com.example.socialmediarestapi.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PostCreationDTO {
    private String text;
    private long referencedPostId;
}
