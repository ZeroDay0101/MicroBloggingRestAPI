package com.example.socialmediarestapi.exception;


import java.util.Iterator;

public class NotFoundException extends RuntimeException implements Iterable<Throwable> {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public Iterator<Throwable> iterator() {
        return null;
    }
}
