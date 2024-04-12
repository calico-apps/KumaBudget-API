package com.calicoapps.kumabudget.security.filter;

import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.security.service.token.JwtService;
import com.calicoapps.kumabudget.security.util.AuthUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {

        try {
            Optional<String> tokenOpt = AuthUtil.getValidTokenFromRequestHeaders(request);

            if (!tokenOpt.isEmpty()) {
                String token = tokenOpt.get();

                String userEmail = jwtService.extractUsername(token);
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                    if (jwtService.isTokenExpired(token) && jwtService.isTokenValidInDB(token)) {
                        Authentication authToken = AuthUtil.buildAuthentication(userDetails, null, request);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }

            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            throw new KumaException(ErrorCode.UNAUTHORIZED_TOKEN);
        }

    }

}