package com.calicoapps.kumabudget.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Optional;

public class AuthUtil {

    public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(10);

    public static Optional<String> getValidTokenFromRequestHeaders(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        } else {
            String token = authHeader.substring(7);
            return Optional.of(token);
        }
    }

    public static Authentication buildAuthentication(Object user, String seasonedPassword, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                seasonedPassword
        );
        if (request != null) {
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }
        return authToken;
    }

}
