package com.humam.security.log;

import com.humam.security.token.TokenRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final TokenRepository tokenRepository;


    public Page<LogResponse> getLogs(String token, Pageable pageable){
        String cleanedToken = token.replaceFirst("^Bearer", "").trim();
        var tokenEntity = tokenRepository.findByToken(cleanedToken)
                .orElseThrow(() -> new IllegalArgumentException("invalid token"));

        var user = tokenEntity.getUser();

        Page<Log> logs = logRepository.findAll(pageable);

        List<LogResponse> logsList = logs.getContent().stream()
                .map(log -> LogResponse.builder()
                        .id(log.getId())
                        .action(log.getAction())
                        .userName(user.fullName())
                        .time(log.getTimestamp())
                        .build()).toList();

        return new PageImpl<>(logsList, pageable, logs.getTotalElements());
    }

    public Page<LogResponse> getByType(String token, LogType type, Pageable pageable){
        String cleanedToken = token.replaceFirst("^Bearer", "").trim();

        var tokenEntity = tokenRepository.findByToken(cleanedToken)
                .orElseThrow(() -> new IllegalArgumentException("invalid token"));

        var user = tokenEntity.getUser();

        Page<Log> logs = logRepository.findByType(type, pageable);

        
        List<LogResponse> logsList = logs.getContent().stream()
                .map(log -> LogResponse.builder()
                        .id(log.getId())
                        .action(log.getAction())
                        .userName(user.fullName())
                        .time(log.getTimestamp())
                        .build()).toList();

        return new PageImpl<>(logsList, pageable, logs.getTotalElements());
    }
}
