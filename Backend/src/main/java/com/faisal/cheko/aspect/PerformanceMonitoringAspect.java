package com.faisal.cheko.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for monitoring performance of service and repository Spring components.
 * Logs execution time for methods and provides warnings for slow methods.
 */
@Aspect
@Component
public class PerformanceMonitoringAspect {

    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitoringAspect.class);
    
    // Threshold for slow method execution in milliseconds
    private static final long EXECUTION_THRESHOLD_MS = 300;

    /**
     * - Pointcut that matches all service methods.
     * - Method is empty as this is just a Pointcut
     */
    @Pointcut("execution(* com.faisal.cheko.service.*.*(..))")
    public void serviceMethodPointcut() {}

    /**
     * - Pointcut that matches all repository methods.
     * - Method is empty as this is just a Pointcut
     */
    @Pointcut("execution(* com.faisal.cheko.repository.*.*(..))")
    public void repositoryMethodPointcut() {}

    /**
     * - Pointcut that matches all controller methods.
     * - Method is empty as this is just a Pointcut
     */
    @Pointcut("execution(* com.faisal.cheko.controller.*.*(..))")
    public void controllerMethodPointcut() {}

    // Advice that monitors method execution time and logs warnings for slow methods.
    @Around("serviceMethodPointcut() || repositoryMethodPointcut() || controllerMethodPointcut()")
    public Object monitorMethodPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            
            if (executionTime > EXECUTION_THRESHOLD_MS) {
                log.warn("SLOW METHOD EXECUTION: {} took {}ms (threshold: {}ms)", 
                        methodName, executionTime, EXECUTION_THRESHOLD_MS);
            } else {
                log.debug("Method {} executed in {}ms", methodName, executionTime);
            }
        }
    }
}
