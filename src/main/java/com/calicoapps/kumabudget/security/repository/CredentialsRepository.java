package com.calicoapps.kumabudget.security.repository;

import com.calicoapps.kumabudget.security.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, String> {

}