package com.example.socialmediarestapi.controller;

import com.example.socialmediarestapi.dto.user.UserLoginDTO;
import com.example.socialmediarestapi.security.authentication.UserDetailsImplementation;
import com.example.socialmediarestapi.security.authentication.jwt.JWTAuthenticationResponse;
import com.example.socialmediarestapi.security.authentication.jwt.TokenType;
import com.example.socialmediarestapi.service.AuthenticationService;
import com.example.socialmediarestapi.service.JWTService;
import com.example.socialmediarestapi.service.UserDetailsServiceImplementation;
import com.example.socialmediarestapi.utills.CookieUtills;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationService authenticationService;
    private final JWTService jwtService;

    private final UserDetailsServiceImplementation userDetailsServiceImplementation;

    public LoginController(AuthenticationService authenticationService, JWTService jwtService, UserDetailsServiceImplementation userDetailsServiceImplementation) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
    }

    @PostMapping
    public ResponseEntity<JWTAuthenticationResponse> login(
            @RequestBody UserLoginDTO usernamePasswordAuthenticationToken,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws Exception {


        Authentication authentication = authenticationService.authenticateUsernamePassword(usernamePasswordAuthenticationToken);

        generateAccessTokenCookie(httpServletResponse, (UserDetailsImplementation) authentication.getPrincipal());
        generateRefreshTokenCookie(httpServletResponse, (UserDetailsImplementation) authentication.getPrincipal());

        return ResponseEntity.ok().body(null);

    }

    @PostMapping
    @RequestMapping("/refresh_token")
    public ResponseEntity<String> refresh_token(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws Exception {

        String token = CookieUtills.getCookieValue(httpServletRequest, "refresh_token");
        Authentication authentication;
        try {
            authentication = authenticationService.authenticateRefreshToken(token);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("refresh_token expired. Please log-in again", HttpStatus.FORBIDDEN);
        }

        UserDetailsImplementation userDetails = (UserDetailsImplementation) userDetailsServiceImplementation.loadUserByUsername(authentication.getName());


        generateAccessTokenCookie(httpServletResponse, userDetails);

        return ResponseEntity.ok().body(null);

    }


    public void generateAccessTokenCookie(HttpServletResponse httpServletResponse, UserDetailsImplementation userDetailsImplementation) throws Exception {
        HashMap<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("userId", (userDetailsImplementation.getUserId()));
        extraClaims.put("roles", userDetailsImplementation.getAuthorities());

        String accessToken = jwtService.generateToken(extraClaims, userDetailsImplementation, TokenType.ACCESS_TOKEN);


        Cookie accessTokenCookie = new Cookie("access_token", accessToken);

        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(jwtService.extractExpiration(accessToken).getTime()));
        accessTokenCookie.setPath("/");


        httpServletResponse.addCookie(accessTokenCookie);
    }

    public void generateRefreshTokenCookie(HttpServletResponse httpServletResponse, UserDetailsImplementation userDetailsImplementation) throws Exception {

        String refreshToken = jwtService.generateToken(userDetailsImplementation, TokenType.REFRESH_TOKEN);


        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);


        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(jwtService.extractExpiration(refreshToken).getTime()));
        refreshTokenCookie.setPath("/login/refresh_token");


        httpServletResponse.addCookie(refreshTokenCookie);


    }

}
