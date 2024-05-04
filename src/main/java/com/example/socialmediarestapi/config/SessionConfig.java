package com.example.socialmediarestapi.config;

import com.example.socialmediarestapi.repository.ProfileRepository;
import com.example.socialmediarestapi.repository.UserRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    public SessionConfig(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }


//    /**
//     * Every logged-in user will have his own Profile instance
//     * @return Profile instance
//     */
//    @Bean()
//    @SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public Long profileId() {
//        return profileRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(
//                NoSuchElementException::new)
//                .getId();
//
//    }
//    /**
//     * Every logged-in user will have his own Profile instance
//     * @return Profile instance
//     */
//    @Bean()
//    @SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public User user() {
//        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(
//                NoSuchElementException::new
//        );
//    }

}
