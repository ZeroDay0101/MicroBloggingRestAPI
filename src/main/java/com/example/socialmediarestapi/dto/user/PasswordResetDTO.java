package com.example.socialmediarestapi.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordResetDTO {
    private final long userId;
    private final String previousPassword;

    @Size(min = UserCreationDTO.minPassword)
    private final String newPassword;

}
