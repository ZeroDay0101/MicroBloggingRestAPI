package com.example.socialmediarestapi.repository;


import com.example.socialmediarestapi.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends UsernameJpaRepository<User, Long> {

}
