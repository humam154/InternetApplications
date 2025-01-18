package com.humam.security.aspect;


import com.humam.security.files.UploadRequest;
import com.humam.security.group.Group;
import com.humam.security.group.GroupRepository;
import com.humam.security.token.TokenRepository;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@AllArgsConstructor
public class FileUploadAspect {

    private final TokenRepository tokenRepository;
    private final GroupRepository groupRepository;
    final Logger logger = LoggerFactory.getLogger(FileUploadAspect.class);


    @Pointcut("execution(* com.humam.security.files.FileService.uploadFile(..))")
    public void fileUploadPointcut() {}



    @Before("fileUploadPointcut()")
    public void logBeforeFileUpload(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String token = (String) args[0];
        UploadRequest request = (UploadRequest) args[1];

        User user = tokenRepository.findByToken(token.replaceFirst(("^Bearer "), ""))
                .orElseThrow(() -> new IllegalArgumentException("invalid token")).getUser();

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("group not found"));

        logger.info("Attempting to upload file: {} to group: {} by user: {} {{}} at {}",
                request.getFile().getOriginalFilename(),
                group.getName(),
                user.fullName(), user.getEmail(),
                Instant.now()
        );
    }

    @After("fileUploadPointcut()")
    public void logAfterFileUpload(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String token = (String) args[0];
        UploadRequest request = (UploadRequest) args[1];


        User user = tokenRepository.findByToken(token.replaceFirst("^Bearer ", ""))
                .orElseThrow(() -> new IllegalArgumentException("invalid token")).getUser();

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("group not found"));

        logger.info("File uploaded successfully: {} to group: {} by user: {} {{}} at {}",
                request.getFile().getOriginalFilename(),
                group.getName(),
                user.fullName(), user.getEmail(),
                Instant.now()
        );
    }


}
