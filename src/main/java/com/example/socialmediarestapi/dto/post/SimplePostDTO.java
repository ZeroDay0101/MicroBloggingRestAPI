package com.example.socialmediarestapi.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimplePostDTO {
    private final long id;
    private final String text;
    private final String postCreatorUsername;
}
