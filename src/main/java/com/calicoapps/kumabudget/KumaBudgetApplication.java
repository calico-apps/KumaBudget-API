package com.calicoapps.kumabudget;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@Slf4j
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
@RequiredArgsConstructor
public class KumaBudgetApplication implements CommandLineRunner {

    private final Environment env;

    public static void main(String[] args) {

        try {
            Dotenv dotenv = Dotenv.load();
            System.setProperty("SPRING_PROFILES_ACTIVE"
                    , dotenv.get("SPRING_PROFILES_ACTIVE"));
            System.setProperty("PORT"
                    , dotenv.get("PORT"));

            System.setProperty("POSTGRES_HOST"
                    , dotenv.get("POSTGRES_HOST"));
            System.setProperty("POSTGRES_DB"
                    , dotenv.get("POSTGRES_DB"));
            System.setProperty("POSTGRES_USER"
                    , dotenv.get("POSTGRES_USER"));
            System.setProperty("POSTGRES_PASSWORD"
                    , dotenv.get("POSTGRES_PASSWORD"));
            System.setProperty("DDL_AUTO"
                    , dotenv.get("DDL_AUTO"));

            System.setProperty("JWT_SECRET"
                    , dotenv.get("JWT_SECRET"));
            System.setProperty("TOKEN_EXPIRATION"
                    , dotenv.get("TOKEN_EXPIRATION"));
            System.setProperty("REFRESH_TOKEN_EXPIRATION"
                    , dotenv.get("REFRESH_TOKEN_EXPIRATION"));
            System.setProperty("PEPPER"
                    , dotenv.get("PEPPER"));
            log.info("=== .env loaded correctly ===");
        } catch (Exception ex) {
            log.info("=== .env file absent or missing required env variables ===");
        }

        SpringApplication.run(KumaBudgetApplication.class, args);

    }

    @Override
    public void run(String... args) {
        log.info("Spring profiles used: {}" , Arrays.asList(env.getActiveProfiles()));
        log.info("Datasource url used: {}" , env.getProperty("spring.datasource.url"));
        log.info("Datasource user used: {}" , env.getProperty("spring.datasource.username"));
    }

}
