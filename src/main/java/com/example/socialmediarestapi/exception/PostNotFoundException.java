package com.example.socialmediarestapi.exception;

public class PostNotFoundException extends NotFoundException {

    public PostNotFoundException(Long id) {
        super("Post with id " + id + " not found.");
    }
}
