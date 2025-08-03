package br.mf.demospringsecurity.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Around("execution(* br.mf.demospringsecurity.controller..*(..)) || " +
            "execution(* br.mf.demospringsecurity.service..*(..))  ")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("Entering {}", methodName);
        if (logger.isDebugEnabled()) {
            logger.debug("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
        }
        Object result = joinPoint.proceed();
        logger.info("Exiting {}", methodName);
        if (logger.isDebugEnabled()) {
            logger.debug("Return: {}", result);
        }
        return result;
    }
}