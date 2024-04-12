package com.calicoapps.kumabudget.security.api;

import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.monitor.LoggingHelper;
import com.calicoapps.kumabudget.security.dto.LoginRequest;
import com.calicoapps.kumabudget.security.dto.RefreshTokenRequest;
import com.calicoapps.kumabudget.security.dto.TokenResponse;
import com.calicoapps.kumabudget.security.service.login.AuthenticationService;
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

    // /api/auth/logout also exists in SecurityConfiguration

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> getToken(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestIdLogLine(request.getMethod(), request.getRequestURI(), loginRequest.getEmail()));
        return ResponseEntity.ok(authenticationService.getToken(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    // To move later
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> registerNewUser(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestIdLogLine(request.getMethod(), request.getRequestURI(), loginRequest.getEmail()));
        return ResponseEntity.ok(authenticationService.generateTokenForNewRegisteredUser(loginRequest));
    }

    // Get a fresh new token without logging in again
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest,
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestSimpleLogLine(request.getMethod(), request.getRequestURI()));
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest.getRefreshToken()));
    }

}
