package com.example.socialmediarestapi.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleProfileDTO {
    private final long id;
    private final String username;
}
