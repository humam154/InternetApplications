package com.humam.security.aspect;


import com.humam.security.token.TokenRepository;
import com.humam.security.user.User;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class FileDownloadAspect {

    private final TokenRepository tokenRepository;
    final Logger logger = LoggerFactory.getLogger(FileUploadAspect.class);

    @Pointcut("execution(* com.humam.security.files.FileService.downloadFile(..))")
    public void fileDownloadPointcut() {}


    @Before("fileDownloadPointcut()")
    public void logBeforeFileDownload(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer fileId = (Integer) args[0];
        String token = (String) args[1];
        User user = tokenRepository.findByToken(token).get().getUser();
        logger.info("Attempting to download file with ID: {}, by user: {}", fileId, user.getEmail());
    }

    @After("fileDownloadPointcut()")
    public void logAfterFileDownload(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer fileId = (Integer) args[0];
        String token = (String) args[1];
        User user = tokenRepository.findByToken(token).get().getUser();
        logger.info("file downloaded file with ID: {}, by user: {}", fileId, user.getEmail());
    }
}
