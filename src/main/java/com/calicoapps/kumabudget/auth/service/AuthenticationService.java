package com.calicoapps.kumabudget.auth.service;

import com.calicoapps.kumabudget.auth.dto.TokenResponse;
import com.calicoapps.kumabudget.auth.entity.Credentials;
import com.calicoapps.kumabudget.common.Device;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CredentialsService credentialsService;
    private final TokenService tokenService;

    public TokenResponse login(String email, String password, Device device) {

        // 1. Verify credentials
        if (!credentialsService.authenticate(email, password)) {
            throw new KumaException(ErrorCode.UNAUTHORIZED_CREDENTIALS);
        }

        // 2. Get Credentials
        Credentials credentials = credentialsService.findById(email);

        // 3. Invalidate old valid tokens of the user
        tokenService.revokeAllTokensForDevice(credentials, device);

        // 2. Generate fresh tokens
        String token = tokenService.generateShortToken(credentials, device);
        String refreshToken = tokenService.generateRefreshToken(credentials, device);

        credentialsService.updateLastActivity(credentials);
        // 4. Return tokens
        return new TokenResponse(credentials.getEmail(), token, refreshToken, device.name());
    }

    public TokenResponse refreshToken(String refreshToken, Device device) {
        if (tokenService.isTokenValid(refreshToken, true, device)) {
            String email = tokenService.extractEmailFromToken(refreshToken);
            Credentials credentials = credentialsService.findById(email);

            tokenService.revokeAllShortTokensForDevice(credentials, device);

            String newToken = tokenService.generateShortToken(credentials, device);

            credentialsService.updateLastActivity(credentials);
            return new TokenResponse(credentials.getEmail(), newToken, refreshToken, device.name());
        } else {
            throw new KumaException(ErrorCode.UNAUTHORIZED_TOKEN);
        }
    }

    public boolean logout(String token, Device device) {
        if (tokenService.isTokenValid(token, false, device)) {
            String email = tokenService.extractEmailFromToken(token);
            Credentials credentials = credentialsService.findById(email);
            tokenService.revokeAllTokensForDevice(credentials, device);
            credentialsService.updateLastActivity(credentials);
            return true;
        } else {
            throw new KumaException(ErrorCode.UNAUTHORIZED_TOKEN);
        }
    }

    public boolean logoutFromAll(String token, Device device) {
        if (tokenService.isTokenValid(token, false, device)) {
            String email = tokenService.extractEmailFromToken(token);
            Credentials credentials = credentialsService.findById(email);
            tokenService.revokeAllTokens(credentials);
            credentialsService.updateLastActivity(credentials);
            return true;
        } else {
            throw new KumaException(ErrorCode.UNAUTHORIZED_TOKEN);
        }
    }

}