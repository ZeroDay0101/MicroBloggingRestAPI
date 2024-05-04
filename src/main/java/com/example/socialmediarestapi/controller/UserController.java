package com.example.socialmediarestapi.controller;

import com.example.socialmediarestapi.dto.user.PasswordResetDTO;
import com.example.socialmediarestapi.dto.user.UserCreationDTO;
import com.example.socialmediarestapi.mappers.UserMapper;
import com.example.socialmediarestapi.security.UserDetailsImplementation;
import com.example.socialmediarestapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
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
            @PathVariable Long id
    ) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) || ((UserDetailsImplementation) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId() == id) {
            userService.deleteUser(userService.getUser(id));
            return ResponseEntity.ok().build();
        } else {
            throw new AccessDeniedException("Forbidden");
        }

    }

    @PatchMapping()
    public ResponseEntity<Void> changeUsername(
            @RequestParam @Size(min = UserCreationDTO.minUsername) @Valid() String username,
            Principal principal
    ) {
        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) principal);

        userService.changeUserName(username, ((UserDetailsImplementation) token.getPrincipal()).getUserId());

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
