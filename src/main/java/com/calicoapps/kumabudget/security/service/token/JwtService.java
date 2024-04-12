package com.calicoapps.kumabudget.security.service.token;

import com.calicoapps.kumabudget.security.entity.Credentials;
import com.calicoapps.kumabudget.security.entity.Token;
import com.calicoapps.kumabudget.security.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;

    @Value("${application.security.jwt.secret}")
    private String secretKey;
    @Value("${application.security.jwt.token.expiration}")
    private long tokenExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    // ==== CLAIMS INFORMATIONS EXTRACTION FROM TOKEN =====================

    // Extract a given claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ==== TOKEN GENERATION =====================

    public String generateAndSaveToken(Credentials credentials) {
        String token = buildToken(credentials, tokenExpiration);
        saveTokenInDB(credentials, token, false);
        return token;
    }

    public String generateAndSaveRefreshToken(Credentials credentials) {
        String refreshToken = buildToken(credentials, refreshTokenExpiration);
        saveTokenInDB(credentials, refreshToken, true);
        return refreshToken;
    }

    private String buildToken(
            UserDetails credentials,
            long expiration
    ) {
        long now = System.currentTimeMillis();

        return Jwts
                .builder()
                .setClaims(new HashMap<>()) // Extra claims like ROLE can be added later
                .setSubject(credentials.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void saveTokenInDB(Credentials credentials, String tokenString, boolean isRefresh) {
        Token token = Token.builder()
                .credentials(credentials)
                .token(tokenString)
                .refresh(isRefresh)
                .expired(false)
                .revoked(false)
                .build();
        token = tokenRepository.save(token);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ==== TOKEN VALIDITY =====================

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValidInDB(String tokenString) {
        Optional<Token> tokenOptional = tokenRepository.findById(tokenString);
        return tokenOptional.filter(token ->
                        !token.isRefresh()
                                && !token.isExpired()
                                && !token.isRevoked()
                                && token.getCredentials().getEmail().equals(extractUsername(tokenString)))
                .isPresent();
    }

    public boolean isRefreshTokenValidInDB(String tokenString) {
        Optional<Token> tokenOptional = tokenRepository.findById(tokenString);
        return tokenOptional.filter(token ->
                        token.isRefresh()
                                && !token.isExpired()
                                && !token.isRevoked()
                                && token.getCredentials().getEmail().equals(extractUsername(tokenString)))
                .isPresent();
    }

}