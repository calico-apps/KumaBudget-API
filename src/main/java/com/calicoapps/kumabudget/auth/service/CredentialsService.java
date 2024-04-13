package com.calicoapps.kumabudget.auth.service;

import com.calicoapps.kumabudget.auth.entity.Credentials;
import com.calicoapps.kumabudget.auth.repository.CredentialsRepository;
import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsService extends DataService<Credentials, String> {

    protected static final String ENTITY_NAME = "Credentials";

    private final CredentialsRepository repository;
    private final CredentialsRepository credentialsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    @Value("${application.security.pepper}")
    private String pepper;

    public CredentialsService(CredentialsRepository repository, CredentialsRepository credentialsRepository) {
        super(ENTITY_NAME);
        this.repository = repository;
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    protected JpaRepository<Credentials, String> getRepository() {
        return repository;
    }

    public Credentials create(String email, String password) {
        if (!doesUserExist(email)) {
            Credentials credentials = new Credentials();
            credentials.setEmail(email);
            credentials.setPassword(bCryptPasswordEncoder.encode(seasonPassword(password)));
            credentials.setActive(true);
            return repository.save(credentials);
        } else {
            throw new KumaException(ErrorCode.BAD_REQUEST_USER_ALREADY_EXIST);
        }
    }

    public boolean doesUserExist(String email) {
        return credentialsRepository.findById(email).isPresent();
    }

    public boolean authenticate(String email, String password) {
        Optional<Credentials> credentialsOpt =
                credentialsRepository.findById(email);
        if (credentialsOpt.isPresent()) {
            Credentials credentials = credentialsOpt.get();
            return bCryptPasswordEncoder.matches(seasonPassword(password), credentials.getPassword());
        }
        return false;
    }

    private String seasonPassword(String password) {
        return password + pepper;
    }

}