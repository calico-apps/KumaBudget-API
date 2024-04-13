package com.calicoapps.kumabudget.auth.service;

import com.calicoapps.kumabudget.auth.entity.Credentials;
import com.calicoapps.kumabudget.auth.entity.Token;
import com.calicoapps.kumabudget.auth.repository.TokenRepository;
import com.calicoapps.kumabudget.common.Device;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Value("${application.security.jwt.secret}")
    private String secretKey;
    @Value("${application.security.jwt.token.expiration}")
    private long tokenExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String generateShortToken(Credentials credentials, Device device) {
        String tokenString = hashUUIDWithSecretKey(UUID.randomUUID(), secretKey);
        long now = System.currentTimeMillis();
        LocalDateTime expiration = LocalDateTime.ofInstant(Instant.ofEpochMilli(now + tokenExpiration), ZoneId.systemDefault());
        Token token = new Token(credentials, tokenString, false, expiration, device);
        token = tokenRepository.save(token);
        return token.getToken();
    }

    public String generateRefreshToken(Credentials credentials, Device device) {
        String tokenString = hashUUIDWithSecretKey(UUID.randomUUID(), secretKey);
        long now = System.currentTimeMillis();
        LocalDateTime expiration = LocalDateTime.ofInstant(Instant.ofEpochMilli(now + refreshTokenExpiration), ZoneId.systemDefault());
        Token token = new Token(credentials, tokenString, true, expiration, device);
        token = tokenRepository.save(token);
        return token.getToken();
    }

    public boolean isTokenValid(String tokenString, boolean isRefreshToken, Device fromDevice) {
        Optional<Token> tokenOptional = tokenRepository.findById(tokenString);

        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            return token.isRefresh() == isRefreshToken
                    && token.getDevice().equals(fromDevice)
                    && token.getExpirationDateTime().isAfter(LocalDateTime.now());
        }
        return false;
    }

    public void revokeAllTokensForDevice(Credentials credentials, Device device) {
        List<Token> tokens = tokenRepository.findAllByCredentialsAndDevice(credentials, device);
        tokenRepository.deleteAll(tokens);
    }

    public void revokeAllTokens(Credentials credentials) {
        List<Token> tokens = tokenRepository.findAllByCredentials(credentials);
        tokenRepository.deleteAll(tokens);
    }

    public void revokeAllShortTokensForDevice(Credentials credentials, Device device) {
        List<Token> shortTokens = tokenRepository.findAllByCredentialsAndRefreshAndDevice(credentials, false, device);
        tokenRepository.deleteAll(shortTokens);
    }

    public String extractEmailFromToken(String tokenString) {
        Token token = tokenRepository.findById(tokenString).orElseThrow(() -> new KumaException(ErrorCode.INTERNAL));
        return token.getCredentials().getEmail();
    }

    private String hashUUIDWithSecretKey(UUID uuid, String secretKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(uuid.toString().getBytes(StandardCharsets.UTF_8));
            md.update(secretKey.getBytes(StandardCharsets.UTF_8));

            byte[] hashedBytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x" , b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new KumaException(ErrorCode.INTERNAL);
        }
    }

}
