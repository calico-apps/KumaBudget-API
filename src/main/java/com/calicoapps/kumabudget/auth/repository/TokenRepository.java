package com.calicoapps.kumabudget.auth.repository;

import com.calicoapps.kumabudget.auth.entity.Credentials;
import com.calicoapps.kumabudget.auth.entity.Token;
import com.calicoapps.kumabudget.common.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {

    List<Token> findAllByCredentials(Credentials credentials);

    List<Token> findAllByCredentialsAndRefreshAndDevice(Credentials credentials, boolean refresh, Device device);

    List<Token> findAllByCredentialsAndDevice(Credentials credentials, Device device);

}