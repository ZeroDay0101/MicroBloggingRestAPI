package com.example.socialmediarestapi.security.authentication.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthenticationResponse {
    private String accessToken;
    private String refreshToken;
}
