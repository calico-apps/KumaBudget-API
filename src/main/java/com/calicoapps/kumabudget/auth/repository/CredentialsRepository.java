package com.calicoapps.kumabudget.auth.repository;

import com.calicoapps.kumabudget.auth.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, String> {

}