package com.calicoapps.kumabudget.auth.api.controller;

import com.calicoapps.kumabudget.auth.api.request.LoginRequest;
import com.calicoapps.kumabudget.auth.api.request.RefreshTokenRequest;
import com.calicoapps.kumabudget.auth.api.response.TokenResponse;
import com.calicoapps.kumabudget.auth.data.token.TokenRepository;
import com.calicoapps.kumabudget.auth.service.login.AuthenticationService;
import com.calicoapps.kumabudget.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.API_URL + "auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationService authenticationService;
    private final TokenRepository tokenRepository;

    // /api/auth/logout also exists in SecurityConfiguration

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> getToken(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} email:" , request.getMethod(), request.getRequestURI(), loginRequest.getEmail());
        return ResponseEntity.ok(authenticationService.getToken(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    // To move later
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> registerNewUser(
            @RequestBody LoginRequest LoginRequest,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} email:" , request.getMethod(), request.getRequestURI(), LoginRequest.getEmail());
        return ResponseEntity.ok(authenticationService.generateTokenForNewRegisteredUser(LoginRequest));
    }

    // Get a fresh new token without logging in again
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest,
            HttpServletRequest request
    ) {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest.getRefreshToken()));
    }

}
