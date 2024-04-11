package com.calicoapps.kumabudget.auth.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser, String> {

}