package com.calicoapps.kumabudget.security.service.user;

import com.calicoapps.kumabudget.security.data.user.AuthUser;
import com.calicoapps.kumabudget.security.data.user.AuthUserRepository;
import com.calicoapps.kumabudget.error.KumaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String ENTITY_NAME = "AuthUser";

    private final AuthUserRepository authUserRepository;

    public AuthUser findUserByEmail(String email) {
        Optional<AuthUser> userOptional = authUserRepository.findById(email);
        return KumaException.orElseThrow(userOptional, ENTITY_NAME, email);
    }

    public Optional<AuthUser> findOptionalUserByEmail(String email) {
        return authUserRepository.findById(email);
    }

}