package fr.clementjaminion.macaronsbackend.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* fr.clementjaminion.macaronsbackend.service..*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        logger.info("Executing method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* fr.clementjaminion.macaronsbackend.service..*(..))", returning = "result")
    public void logAfterMethodExecution(JoinPoint joinPoint, Object result) {
        logger.info("Method executed: {} with result: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "execution(* fr.clementjaminion.macaronsbackend.service..*(..))", throwing = "error")
    public void logAfterMethodError(JoinPoint joinPoint, Throwable error) {
        logger.error("Method {} threw an error: {}", joinPoint.getSignature().getName(), error.getMessage());
    }
}
