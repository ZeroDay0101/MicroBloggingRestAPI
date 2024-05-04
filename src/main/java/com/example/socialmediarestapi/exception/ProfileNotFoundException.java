package com.example.socialmediarestapi.exception;

public class ProfileNotFoundException extends NotFoundException {
    public ProfileNotFoundException(Long id) {
        super("Profile with id " + id + " not found.");
    }

    public ProfileNotFoundException(String message) {
        super("Profile with username " + message + " not found.");
    }
}
