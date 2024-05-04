package com.example.socialmediarestapi.service;

import com.example.socialmediarestapi.model.entity.User;
import com.example.socialmediarestapi.security.UserDetailsImplementation;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImplementation(UserService userService, ProfileService profileService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUser(username);

        return new UserDetailsImplementation(user.getUsername(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())), user.getId());
    }
}
