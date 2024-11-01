package org.example.taskmanagementapi.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class ExecutionTimeAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Around("@annotation(org.example.taskmanagementapi.annotations.TrackExecutionTime)")
    public Object trackExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object res = joinPoint.proceed();
        stopWatch.stop();
        logger.warn("{} executed in {} ms",joinPoint.getSignature(), stopWatch.getTotalTimeMillis());
        return res;
    }
}
