package com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("@annotation(com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging.Loggable)")
    void annotatedLoggablePointcut() {

    }

    @Around("annotatedLoggablePointcut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String methodName = joinPoint.getSignature().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findAny()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElseThrow();
        Object[] args = joinPoint.getArgs();
        stopWatch.start();
        Object toBeReturned = joinPoint.proceed();
        stopWatch.stop();
        double deltaMs = stopWatch.lastTaskInfo().getTime(TimeUnit.MILLISECONDS);
        log.trace("{} {} called {} with args {} took {} ms", role,userName, methodName, args, deltaMs);
        return toBeReturned;
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    void annotatedServicePointcut() {

    }
}
