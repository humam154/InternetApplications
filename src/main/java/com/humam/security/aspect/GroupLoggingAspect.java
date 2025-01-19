package com.humam.security.aspect;

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

@Aspect
@Component
@AllArgsConstructor
public class GroupLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(GroupLoggingAspect.class);
    private final TokenRepository tokenRepository;

    @Pointcut("execution(* com.humam.security.group.GroupController.*(..))")
    public void logGroupsPointcut() {}


    @After("logGroupsPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String token = (String) args[0];
        User user = tokenRepository.findByToken(token.replaceFirst("^Bearer ", ""))
                .orElseThrow(() -> new IllegalArgumentException("invalid token")).getUser();
        long startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder("Groups Custom Logging:");
        sb.append("[").append(joinPoint.getKind()).append("]'\tfor: ").append(joinPoint.getSignature())
                .append("\tfrom user: ").append("(").append(user.fullName())
                .append(")");
        sb.append("\ttook: ");
        logger.info(sb.append(System.currentTimeMillis() - startTime).append(" ms.").toString());
    }
}
