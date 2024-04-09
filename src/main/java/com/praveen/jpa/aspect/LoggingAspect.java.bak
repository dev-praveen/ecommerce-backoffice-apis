package com.praveen.jpa.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Before("execution(* com.praveen.jpa..*.* (..))")
  public void logBefore(JoinPoint joinPoint) {
    logger.info(
        "Entering method: {} with arguments: {} ,Class Name:{}",
        joinPoint.getSignature().getName(),
        joinPoint.getArgs(),
        joinPoint.getSignature().getDeclaringTypeName());
  }

  @AfterReturning(pointcut = "execution(* com.praveen.jpa..*.* (..))", returning = "result")
  public void logAfter(JoinPoint joinPoint, Object result) {
    logger.info(
        "Exiting method: {} with result: {} ,Class Name:{}",
        joinPoint.getSignature().getName(),
        result,
        joinPoint.getSignature().getDeclaringTypeName());
  }
}
