package com.calicoapps.kumabudget.security.service.login;

import com.calicoapps.kumabudget.security.data.token.Token;
import com.calicoapps.kumabudget.security.data.token.TokenRepository;
import com.calicoapps.kumabudget.security.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {

        Optional<String> tokenOpt = AuthUtil.getValidTokenFromRequestHeaders(request);

        if (tokenOpt.isEmpty()) {
            return;
        }

        String token = tokenOpt.get();
        Token storedToken = tokenRepository.findByToken(token)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}