package com.example.socialmediarestapi.dto.profile;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class ProfileDTO {
    private String username;
    private int followers;
    private int following;
    private int likes;
    private int postCount;
    private LocalDate creationDate;
}
