package com.calicoapps.kumabudget.security.api.filter;

import com.calicoapps.kumabudget.security.data.token.TokenRepository;
import com.calicoapps.kumabudget.security.service.token.JwtService;
import com.calicoapps.kumabudget.security.util.AuthUtil;
import com.calicoapps.kumabudget.error.ErrorCode;
import com.calicoapps.kumabudget.error.KumaException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService JwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {

        // 1. Retrieve token header

        Optional<String> tokenOpt = AuthUtil.getValidTokenFromRequestHeaders(request);

        if (tokenOpt.isEmpty()) {
            // The check if the endpoint is allowed to be unprotected is done after the doFilter
            filterChain.doFilter(request, response);
            return;
        }

        String token = tokenOpt.get();

        // 2. Retrieve the user credentials based on the token if it's valid
        // TODO: to rework
        try {
            String userEmail = JwtService.extractUsername(token);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (JwtService.isTokenValid(token, userDetails) && isTokenValid(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JwtException ex) {
            throw new KumaException(ErrorCode.UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .map(tk -> !tk.isExpired() && !tk.isRevoked())
                .orElse(false);
    }


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String token = getJWTfromRequest(request);
//        if(token != null && jwtGenerator.validateToken(token)) {
//            String username = jwtGenerator.getUsernameFromJWT(token);
//            String userType = jwtGenerator.getUserTypeFromJWT(token);
//            customUserDetailsService.setUserType(UserType.valueOf(userType));
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
//                    null, userDetails.getAuthorities());
//
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        }
//        filterChain.doFilter(request, response);
//
//    }

}