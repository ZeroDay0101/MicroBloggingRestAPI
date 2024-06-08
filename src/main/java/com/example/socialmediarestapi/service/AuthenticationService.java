package com.example.socialmediarestapi.service;

import com.example.socialmediarestapi.dto.user.UserLoginDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
    }

    public Authentication authenticateUsernamePassword(
            @RequestBody UserLoginDTO usernamePasswordAuthenticationToken
    ) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(usernamePasswordAuthenticationToken.getUsername(), usernamePasswordAuthenticationToken.getPassword());

        return authenticationManager.authenticate(authenticationRequest);
    }

    public Authentication authenticateRefreshToken(String token) {
        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = new BearerTokenAuthenticationToken(token);

        return authenticationManager.authenticate(bearerTokenAuthenticationToken);
    }

}
