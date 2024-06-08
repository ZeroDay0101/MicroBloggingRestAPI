package com.example.socialmediarestapi.exception;

public class CookieNotFoundException extends NotFoundException {
    public CookieNotFoundException(String cookieName) {
        super("Cookie with name " + cookieName + " not found");
    }
}
