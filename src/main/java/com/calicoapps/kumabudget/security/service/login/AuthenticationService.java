package com.calicoapps.kumabudget.security.service.login;

import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.security.dto.TokenResponse;
import com.calicoapps.kumabudget.security.entity.Credentials;
import com.calicoapps.kumabudget.security.entity.Token;
import com.calicoapps.kumabudget.security.repository.TokenRepository;
import com.calicoapps.kumabudget.security.service.token.JwtService;
import com.calicoapps.kumabudget.security.service.user.CredentialsService;
import com.calicoapps.kumabudget.security.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CredentialsService credentialsService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse getToken(String email, String password) {

        // 1. Verify credentials
        try {
            authenticationManager.authenticate(
                    AuthUtil.buildAuthentication(email, credentialsService.seasonPassword(password), null)
            );
        } catch (BadCredentialsException e) {
            throw new KumaException(ErrorCode.UNAUTHORIZED_CREDENTIALS);
        }

        // 2. Generate fresh tokens
        Credentials credentials = credentialsService.findById(email);
        String token = jwtService.generateToken(credentials);
        String refreshToken = jwtService.generateRefreshToken(credentials);

        // 3. Invalidate old valid tokens of the user
        revokeAllUserTokens(credentials);

        // 4. Return tokens
        return TokenResponse.builder()
                .userEmail(credentials.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(Credentials credentials) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(credentials.getEmail());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public TokenResponse refreshToken(String refreshToken) {

        // 1. refreshToken empty
        if (refreshToken == null) {
            throw new KumaException(ErrorCode.UNAUTHORIZED_TOKEN);
        }

        String userEmail = jwtService.extractUsername(refreshToken);

        // Token doesn't have a user linked
        if (userEmail == null) {
            throw new KumaException(ErrorCode.UNAUTHORIZED_TOKEN);
        }

        Credentials credentials = credentialsService.findById(userEmail);

        if (jwtService.isRefreshTokenValidInDB(refreshToken)) {
            String accessToken = jwtService.generateToken(credentials);

            revokeAllUserTokens(credentials);

            return TokenResponse.builder()
                    .userEmail(credentials.getEmail())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new KumaException(ErrorCode.UNAUTHORIZED_TOKEN);
        }

    }

}