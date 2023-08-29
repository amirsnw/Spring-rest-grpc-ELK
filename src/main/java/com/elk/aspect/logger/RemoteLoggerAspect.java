package com.elk.aspect.logger;

import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Aspect
public class RemoteLoggerAspect {

    @Before("@annotation(RemoteLogger)")
    @SneakyThrows
    public void afterReturningWithAnnotation(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = (signature).getMethod();
        RemoteLogger logger = method.getAnnotation(RemoteLogger.class);
        String payload = logger.value();
        Logger log = LoggerFactory.getLogger(method.getDeclaringClass());
        Method loggerMethod = log.getClass()
                .getDeclaredMethod(logger.type().name().toLowerCase(), String.class, Object[].class);

        Map<String, String> arguments = new HashMap<>();
        Iterator<Object> paramValues = Arrays.stream(joinPoint.getArgs()).iterator();
        Iterator<String> paramNames = Arrays.stream(signature.getParameterNames()).iterator();
        while (paramValues.hasNext()) {
            arguments.put(paramNames.next(), paramValues.next().toString().trim());
        }

        try (var ignored = MDC.putCloseable("isAspect", "true")) {
            loggerMethod.invoke(log, payload, new Object[]{arguments});
        }
    }

    @Around("@within(com.elk.aspect.logger.RemoteLogger)")
    public Object applyAspectToMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        //TODO: Apply the same logic to class level (all inner methods)
        return joinPoint.proceed();
    }
}
