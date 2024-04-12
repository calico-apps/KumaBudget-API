package com.calicoapps.kumabudget.security.config;

import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.security.repository.CredentialsRepository;
import com.calicoapps.kumabudget.security.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityBeansConfiguration {
    // Beans must be in a separated configuration than SecurityConfig class because of circular ref

    //    private final CredentialsService credentialsService;
    private final CredentialsRepository credentialsRepository;

    @Bean // Retrieve user (= UserDetails) informations
    public UserDetailsService userDetailsService() {
        return email -> credentialsRepository.findById(email).orElseThrow(() -> new KumaException(ErrorCode.UNAUTHORIZED_CREDENTIALS));
    }

    @Bean // Manage authentications
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean // Verify user credentials
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean // Encode password
    public PasswordEncoder passwordEncoder() {
        return AuthUtil.PASSWORD_ENCODER;
    }

}
