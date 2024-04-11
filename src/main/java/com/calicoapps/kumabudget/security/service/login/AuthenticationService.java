package com.calicoapps.kumabudget.security.service.login;

import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.security.dto.LoginRequest;
import com.calicoapps.kumabudget.security.dto.TokenResponse;
import com.calicoapps.kumabudget.security.entity.Credentials;
import com.calicoapps.kumabudget.security.entity.Token;
import com.calicoapps.kumabudget.security.repository.CredentialsRepository;
import com.calicoapps.kumabudget.security.repository.TokenRepository;
import com.calicoapps.kumabudget.security.service.token.JwtService;
import com.calicoapps.kumabudget.security.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CredentialsRepository credentialsRepository;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService JwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${application.security.pepper}")
    private String pepper;

    public TokenResponse generateTokenForNewRegisteredUser(LoginRequest request) {

        // 1. Create new user in DB with encoded password
        Credentials newCredentials = new Credentials();
        newCredentials.setEmail(request.getEmail());
        newCredentials.setPassword(encodeSeasonedPassword(request.getPassword()));
        newCredentials = credentialsRepository.save(newCredentials);

        // 2. Create token
        String token = JwtService.generateToken(newCredentials);
        String refreshToken = JwtService.generateRefreshToken(newCredentials);
        saveUserToken(newCredentials, token);

        // 3. Return the token
        return TokenResponse.builder()
                .userEmail(newCredentials.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();

    }

    public TokenResponse getToken(String email, String password) {

        // 1. Verify credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        seasonPassword(password)
                )
        );

        // 2. Generate new tokens
        Credentials credentials = userService.findById(email);
        String token = JwtService.generateToken(credentials);
        String refreshToken = JwtService.generateRefreshToken(credentials);

        // 3. Invalidate old valid tokens of the user
        revokeAllUserTokens(credentials);

        // 4. Save new generated valid token
        saveUserToken(credentials, token);

        // 5. Return tokens
        return TokenResponse.builder()
                .userEmail(credentials.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(Credentials credentials, String jwtToken) {
        Token token = Token.builder()
                .credentials(credentials)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
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

//    public boolean isTokenValid(String authHeader) {
//
//        // 1. Header not valid
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new KumaException(ErrorCode.NOT_AUTHORIZED);
//        }
//
//        String token = authHeader.substring(7);
//        String userEmail = jwtService.extractUsername(token);
//
//        return jwtService.isTokenValid(token, userService.findUserByEmail(userEmail));
//    }

    public TokenResponse refreshToken(
            String refreshToken
    ) {
        //final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 1. Header not valid
        if (refreshToken == null) {
            throw new KumaException(ErrorCode.UNAUTHORIZED);
        }

//        String refreshToken = authHeader.substring(7);
        String userEmail = JwtService.extractUsername(refreshToken);

        // Token doesn't have a user linked
        if (userEmail == null) {
            throw new KumaException(ErrorCode.UNAUTHORIZED);
        }

        Credentials credentials = userService.findById(userEmail);

        if (JwtService.isTokenValid(refreshToken, credentials)) {
            String accessToken = JwtService.generateToken(credentials);

            revokeAllUserTokens(credentials);
            saveUserToken(credentials, accessToken);

            return TokenResponse.builder()
                    .userEmail(credentials.getEmail())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new KumaException(ErrorCode.UNAUTHORIZED);
        }

    }

    private String seasonPassword(String password) {
        return password + pepper;
    }

    private String encodeSeasonedPassword(String password) {
        return passwordEncoder.encode(seasonPassword(password));
    }

}