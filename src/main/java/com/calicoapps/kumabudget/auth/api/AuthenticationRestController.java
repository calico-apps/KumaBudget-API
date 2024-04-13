package com.calicoapps.kumabudget.auth.api;

import com.calicoapps.kumabudget.auth.dto.CredentialsRequest;
import com.calicoapps.kumabudget.auth.dto.RefreshTokenRequest;
import com.calicoapps.kumabudget.auth.dto.TokenResponse;
import com.calicoapps.kumabudget.auth.service.AuthenticationService;
import com.calicoapps.kumabudget.auth.service.CredentialsService;
import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.common.Device;
import com.calicoapps.kumabudget.common.util.HeadersUtil;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.monitor.LoggingHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.API_URL + "auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;
    private final CredentialsService credentialsService;

    @PostMapping("/login")
    @Operation(summary = "Login and get fresh Token and Refresh Token")
    public ResponseEntity<TokenResponse> login(
            @RequestBody CredentialsRequest credentialsRequest,
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestIdLogLine(request.getMethod(), request.getRequestURI(), credentialsRequest.getEmail()));
        Device device = HeadersUtil.getValidDeviceFromRequestHeaders(request).get(); // Optional can't be null thanks to interceptor
        return ResponseEntity.ok(authenticationService.login(credentialsRequest.getEmail(), credentialsRequest.getPassword(), device));
    }

    @PutMapping("/logout")
    @Operation(summary = "Invalidates all the tokens for the given device")
    public ResponseEntity<String> logout(
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestSimpleLogLine(request.getMethod(), request.getRequestURI()));
        String token = HeadersUtil.getValidTokenFromRequestHeaders(request).orElseThrow(() -> new KumaException(ErrorCode.UNAUTHORIZED_TOKEN));
        Device device = HeadersUtil.getValidDeviceFromRequestHeaders(request).get();
        return ResponseEntity.ok(authenticationService.logout(token, device) ? "OK" : "NOK");
    }

    @PutMapping("/logout/all")
    @Operation(summary = "Invalidates all the tokens")
    public ResponseEntity<String> logoutAll(
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestSimpleLogLine(request.getMethod(), request.getRequestURI()));
        String token = HeadersUtil.getValidTokenFromRequestHeaders(request).orElseThrow(() -> new KumaException(ErrorCode.UNAUTHORIZED_TOKEN));
        Device device = HeadersUtil.getValidDeviceFromRequestHeaders(request).get();
        return ResponseEntity.ok(authenticationService.logoutFromAll(token, device) ? "OK" : "NOK");
    }

    //    // To move later
    @PostMapping("/register")
    @Operation(summary = "Temporary")
    public ResponseEntity registerNewUser(
            @RequestBody CredentialsRequest credentialsRequest,
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestIdLogLine(request.getMethod(), request.getRequestURI(), credentialsRequest.getEmail()));
        credentialsService.create(credentialsRequest.getEmail(), credentialsRequest.getPassword());
        return ResponseEntity.ok().build();
    }

    // Get a fresh new token without logging in again
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest,
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestSimpleLogLine(request.getMethod(), request.getRequestURI()));
        Device device = HeadersUtil.getValidDeviceFromRequestHeaders(request).get();
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest.getRefreshToken(), device));
    }

}
