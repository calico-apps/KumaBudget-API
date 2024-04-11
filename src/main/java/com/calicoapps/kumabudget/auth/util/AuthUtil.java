package com.calicoapps.kumabudget.auth.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class AuthUtil {

    public static Optional<String> getValidTokenFromRequestHeaders(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        } else {
            String token = authHeader.substring(7);
            return Optional.of(token);
        }
    }

}
