package com.example.socialmediarestapi.service;

import com.example.socialmediarestapi.exception.ProfileNotFoundException;
import com.example.socialmediarestapi.model.entity.Profile;
import com.example.socialmediarestapi.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional(readOnly = true)
    public Profile getProfile(Long id) {
        return profileRepository.findById(id).orElseThrow(
                () -> new ProfileNotFoundException(id)
        );
    }

    @Transactional(readOnly = true)
    public Profile getProfile(String username) {
        return profileRepository.findByUsername(username).orElseThrow(
                () -> new ProfileNotFoundException(username)
        );
    }

    @Transactional(readOnly = true)
    public Long getProfileIdByUsername(String username) {
        return profileRepository.findByUsername(username).orElseThrow(
                () -> new ProfileNotFoundException(username)
        ).getId();
    }


}
