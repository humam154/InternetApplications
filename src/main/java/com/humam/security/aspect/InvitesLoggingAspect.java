package com.humam.security.aspect;

import com.humam.security.log.Log;
import com.humam.security.log.LogRepository;
import com.humam.security.log.LogType;
import com.humam.security.token.TokenRepository;
import com.humam.security.user.User;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.humam.security.log.LogType.INVITES;

@Aspect
@Component
@AllArgsConstructor
public class InvitesLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(InvitesLoggingAspect.class);
    private final TokenRepository tokenRepository;
    private final LogRepository logRepository;

    @Pointcut("execution(* com.humam.security.invite.InviteController(..))")
    public void logInvitesPointcut() {}


    @After("logInvitesPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String token = (String) args[0];
        User user = tokenRepository.findByToken(token.replaceFirst("^Bearer ", ""))
                .orElseThrow(() -> new IllegalArgumentException("invalid token")).getUser();
        long startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder("invites Custom Logging:");
        sb.append("[").append(joinPoint.getKind()).append("]'\tfor: ").append(joinPoint.getSignature())
                .append("\tfrom user: ").append("(").append(user.fullName())
                .append(")");
        sb.append("\ttook: ");

        Log log = Log.builder()
                .action("Operation on invites")
                .user(user)
                .logType(INVITES)
                .timestamp(LocalDateTime.now())
                .build();

        logRepository.save(log);
        logger.info(sb.append(System.currentTimeMillis() - startTime).append(" ms.").toString());
    }
}
