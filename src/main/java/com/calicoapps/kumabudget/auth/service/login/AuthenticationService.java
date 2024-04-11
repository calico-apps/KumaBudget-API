package com.calicoapps.kumabudget.auth.service.login;

import com.calicoapps.kumabudget.auth.api.request.LoginRequest;
import com.calicoapps.kumabudget.auth.api.response.TokenResponse;
import com.calicoapps.kumabudget.auth.data.token.Token;
import com.calicoapps.kumabudget.auth.data.token.TokenRepository;
import com.calicoapps.kumabudget.auth.data.user.AuthUser;
import com.calicoapps.kumabudget.auth.data.user.AuthUserRepository;
import com.calicoapps.kumabudget.auth.service.token.JwtService;
import com.calicoapps.kumabudget.auth.service.user.UserService;
import com.calicoapps.kumabudget.error.ErrorCode;
import com.calicoapps.kumabudget.error.KumaException;
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

    private final AuthUserRepository authUserRepository;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService JwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${application.security.pepper}")
    private String pepper;

    public TokenResponse generateTokenForNewRegisteredUser(LoginRequest request) {

        // 1. Create new user in DB with encoded password
        AuthUser newAuthUser = new AuthUser();
        newAuthUser.setEmail(request.getEmail());
        newAuthUser.setPassword(encodeSeasonedPassword(request.getPassword()));
        newAuthUser = authUserRepository.save(newAuthUser);

        // 2. Create token
        String token = JwtService.generateToken(newAuthUser);
        String refreshToken = JwtService.generateRefreshToken(newAuthUser);
        saveUserToken(newAuthUser, token);

        // 3. Return the token
        return TokenResponse.builder()
                .userEmail(newAuthUser.getEmail())
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
        AuthUser authUser = userService.findUserByEmail(email);
        String token = JwtService.generateToken(authUser);
        String refreshToken = JwtService.generateRefreshToken(authUser);

        // 3. Invalidate old valid tokens of the user
        revokeAllUserTokens(authUser);

        // 4. Save new generated valid token
        saveUserToken(authUser, token);

        // 5. Return tokens
        return TokenResponse.builder()
                .userEmail(authUser.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(AuthUser authUser, String jwtToken) {
        Token token = Token.builder()
                .authUser(authUser)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(AuthUser authUser) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(authUser.getEmail());
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

        AuthUser authUser = userService.findUserByEmail(userEmail);

        if (JwtService.isTokenValid(refreshToken, authUser)) {
            String accessToken = JwtService.generateToken(authUser);

            revokeAllUserTokens(authUser);
            saveUserToken(authUser, accessToken);

            return TokenResponse.builder()
                    .userEmail(authUser.getEmail())
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