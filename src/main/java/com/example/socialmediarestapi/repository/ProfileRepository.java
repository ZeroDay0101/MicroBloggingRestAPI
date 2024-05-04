package com.example.socialmediarestapi.repository;

import com.example.socialmediarestapi.model.entity.Profile;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends UsernameJpaRepository<Profile, Long> {


}
