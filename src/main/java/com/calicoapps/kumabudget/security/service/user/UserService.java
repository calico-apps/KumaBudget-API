package com.calicoapps.kumabudget.security.service.user;

import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.security.entity.Credentials;
import com.calicoapps.kumabudget.security.repository.CredentialsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends DataService<Credentials, String> {

    public static final String ENTITY_NAME = "Credentials";

    private final CredentialsRepository repository;

    public UserService(CredentialsRepository repository) {
        super(ENTITY_NAME);
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Credentials, String> getRepository() {
        return repository;
    }

}