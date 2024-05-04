package com.example.socialmediarestapi.exception;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found.");
    }

    public UserNotFoundException(String message) {
        super("User with username " + message + " not found.");
    }
}