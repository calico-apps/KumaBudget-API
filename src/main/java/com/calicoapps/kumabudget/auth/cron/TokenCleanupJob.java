package com.calicoapps.kumabudget.auth.cron;

import com.calicoapps.kumabudget.auth.entity.Token;
import com.calicoapps.kumabudget.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupJob {

    private final TokenRepository tokenRepository;

    //    @Scheduled(fixedRate = 30000)
    @Scheduled(cron = "0 0 0 * * ?")  // Every day at midnight
    public void execute() {
        log.info("CronJob: Cleaning up tokens");
        List<Token> tokens = tokenRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Token token : tokens) {
            if (token.getExpirationDateTime().isBefore(now)) {
                tokenRepository.delete(token);
            }
        }
    }
}