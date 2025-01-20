package com.humam.security.log;

import com.humam.security.token.TokenRepository;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;
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
        var user = tokenRepository.findByToken(cleanedToken)
                .orElseThrow(() -> new IllegalArgumentException("invalid token"))
                .getUser();
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
        token = token.replaceFirst("^Bearer", "");
        User user = tokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalArgumentException("invalid token")
        ).getUser();

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
