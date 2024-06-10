package com.example.socialmediarestapi.controller;

import com.example.socialmediarestapi.dto.user.PasswordResetDTO;
import com.example.socialmediarestapi.dto.user.UserCreationDTO;
import com.example.socialmediarestapi.mappers.UserMapper;
import com.example.socialmediarestapi.service.JWTService;
import com.example.socialmediarestapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    private final JWTService jwtService;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, JWTService jwtService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserCreationDTO userCreationDTO) {
        userService.createUser(
                userMapper.creationDTOToUser(userCreationDTO)
        );


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            Authentication authentication
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) || jwtService.extractUserId(((JwtAuthenticationToken) authentication).getToken().getTokenValue()) == id) {
            userService.deleteUser(userService.getUser(id));
            return ResponseEntity.ok().build();
        } else {
            throw new AccessDeniedException("Forbidden");
        }

    }

    @PatchMapping()
    public ResponseEntity<Void> changeUsername(
            @RequestParam @Size(min = UserCreationDTO.minUsername) @Valid() String newUsername,
            Authentication authentication
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {


        String tokenValue = ((JwtAuthenticationToken) authentication).getToken().getTokenValue();

        long userId = jwtService.extractUserId(tokenValue);

        userService.changeUserName(newUsername, userId);

        return ResponseEntity.ok().body(null);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeUsernameById(
            @RequestParam @Size(min = UserCreationDTO.minUsername) @Valid() String username,
            @PathVariable long id
    ) {

        UserCreationDTO.builder().username(username).build(); //Validation
        userService.changeUserName(username, id);

        return ResponseEntity.ok().body(null);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid PasswordResetDTO passwordResetDTO
    ) {
        userService.changePassword(passwordResetDTO);

        return ResponseEntity.ok().body(null);
    }

}
