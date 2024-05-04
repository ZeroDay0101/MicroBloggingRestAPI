package com.example.socialmediarestapi.service;

import com.example.socialmediarestapi.dto.user.PasswordResetDTO;
import com.example.socialmediarestapi.exception.UserNotFoundException;
import com.example.socialmediarestapi.model.entity.Profile;
import com.example.socialmediarestapi.model.entity.User;
import com.example.socialmediarestapi.repository.UserRepository;
import com.example.socialmediarestapi.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Profile profile = Profile.builder()
                .username(user.getUsername())
                .user(user)
                .build();

        user.setProfile(profile);
        user.setRole(Role.USER);

        userRepository.save(user);

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
    }

    @Transactional(readOnly = true)
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username)
        );
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }


    public void changeUserName(
            String username,
            long id
    ) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));

        user.setUsername(username);
        user.getProfile().setUsername(username);
    }

    public void changePassword(PasswordResetDTO passwordResetDTO) {
        long id = passwordResetDTO.getUserId();
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));

        if (passwordEncoder.matches(passwordResetDTO.getPreviousPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
        }

    }
}
