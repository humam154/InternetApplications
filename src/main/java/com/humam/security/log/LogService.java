package com.humam.security.log;

import com.humam.security.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final TokenRepository tokenRepository;


    public List<LogResponse> getLogs(String token){
        String cleanedToken = token.replaceFirst("^Bearer", "").trim();
        var tokenEntity = tokenRepository.findByToken(cleanedToken)
                .orElseThrow(() -> new IllegalArgumentException("invalid token"));

        var user = tokenEntity.getUser();

        List<Log> logs = logRepository.findAll();

        return logs.stream().map(
                log -> LogResponse.builder()
                        .action(log.getAction())
                        .userName(user.fullName())
                        .time(log.getTimestamp())
                        .build()
        ).toList();
    }

    public List<LogResponse> getByType(String token, LogType type){
        String cleanedToken = token.replaceFirst("^Bearer", "").trim();

        // Find the token in the repository
        var tokenEntity = tokenRepository.findByToken(cleanedToken)
                .orElseThrow(() -> new IllegalArgumentException("invalid token"));

        var user = tokenEntity.getUser();

        List<Log> logs = logRepository.findByType(type);

        return logs.stream().map(
                log -> LogResponse.builder()
                        .action(log.getAction())
                        .userName(user.fullName())
                        .time(log.getTimestamp())
                        .build()
        ).toList();
    }
}
