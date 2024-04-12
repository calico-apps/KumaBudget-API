package com.calicoapps.kumabudget.security.service.user;

import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.security.entity.Credentials;
import com.calicoapps.kumabudget.security.repository.CredentialsRepository;
import com.calicoapps.kumabudget.security.util.AuthUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService extends DataService<Credentials, String> {

    public static final String ENTITY_NAME = "Credentials";

    private final CredentialsRepository repository;
    @Value("${application.security.pepper}")
    private String pepper;

    public CredentialsService(CredentialsRepository repository) {
        super(ENTITY_NAME);
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Credentials, String> getRepository() {
        return repository;
    }

    public Credentials create(String email, String password) {

        // todo: check if not existing before

        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setPassword(encodeSeasonedPassword(password));
        credentials.setActive(true);
        return repository.save(credentials);
    }

    public String seasonPassword(String password) {
        return password + pepper;
    }

    public String encodeSeasonedPassword(String password) {
        return AuthUtil.PASSWORD_ENCODER.encode(seasonPassword(password));
    }

}