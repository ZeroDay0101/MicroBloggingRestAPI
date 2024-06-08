package com.example.socialmediarestapi.utills;

import com.example.socialmediarestapi.exception.CookieNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class CookieUtills {
    public static String getCookieValue(HttpServletRequest req, String cookieName) {
        if (req.getCookies() == null)
            throw new CookieNotFoundException(cookieName);


        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(
                        () -> new CookieNotFoundException(cookieName)
                );
    }
}
