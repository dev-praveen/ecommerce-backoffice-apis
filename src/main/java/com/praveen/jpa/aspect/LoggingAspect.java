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
        "Entering method: {} with arguments: {} ,Class Name:{}, isVirtualThread:{}",
        joinPoint.getSignature().getName(),
        joinPoint.getArgs(),
        joinPoint.getSignature().getDeclaringTypeName(),
        Thread.currentThread().isVirtual());
  }

  @AfterReturning(pointcut = "execution(* com.praveen.jpa..*.* (..))")
  public void logAfter(JoinPoint joinPoint) {
    logger.info(
        "Exiting method: {} ,Class Name:{}, isVirtualThread:{}",
        joinPoint.getSignature().getName(),
        joinPoint.getSignature().getDeclaringTypeName(),
        Thread.currentThread().isVirtual());
  }
}
