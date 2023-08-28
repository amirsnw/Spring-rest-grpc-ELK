package com.elk.aspect.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Aspect
public class RemoteLoggerAspect {

    @Before("@annotation(com.elk.aspect.logger.RemoteLogger)")
    public void afterReturningWithAnnotation(JoinPoint joinPoint)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RemoteLogger logger = method.getAnnotation(RemoteLogger.class);
        String payload = logger.value();
        Logger log = LoggerFactory.getLogger(method.getDeclaringClass());
        Method loggerMethod = log.getClass()
                .getDeclaredMethod(logger.type().name().toLowerCase(), String.class, Object.class);

        loggerMethod.invoke(log, payload);
    }

    @Around("@within(com.elk.aspect.logger.RemoteLogger)")
    public Object applyAspectToMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        //TODO: Apply the same logic to class level (all inner methods)
        return joinPoint.proceed();
    }

}
