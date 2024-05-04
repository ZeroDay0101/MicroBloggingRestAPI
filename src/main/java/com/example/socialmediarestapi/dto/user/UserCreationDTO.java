package com.example.socialmediarestapi.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserCreationDTO {
    //No transfer from config
    public static final int minUsername = 3;
    public static final int minPassword = 4;

    @NotNull
    @Size(min = minUsername)
    private final String username;

    @NotNull
    @Size(min = minPassword)
    private final String password;
}
