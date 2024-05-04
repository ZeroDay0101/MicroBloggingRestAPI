package com.example.socialmediarestapi.controller;

import com.example.socialmediarestapi.dto.profile.ProfileDTO;
import com.example.socialmediarestapi.mappers.ProfileMapper;
import com.example.socialmediarestapi.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @GetMapping
    @RequestMapping("/byUsername/{username}")
    public ResponseEntity<ProfileDTO> getProfileByUsername(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(profileMapper.profileToProfileDTO(
                profileService.getProfile(username)
        ));
    }


    @GetMapping
    @RequestMapping("/byId/{id}")
    public ResponseEntity<ProfileDTO> getProfileById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileMapper.profileToProfileDTO(
                profileService.getProfile(id)
        ));
    }

    @GetMapping
    @RequestMapping("/getId/byUsername/{username}")
    public ResponseEntity<Long> getProfileIdByUsername(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(profileService.getProfileIdByUsername(username));
    }

}
