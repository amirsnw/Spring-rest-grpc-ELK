package com.elk.aspect.logger;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class RemoteLoggerAspect {

    private static String extractPayload(RemoteLogger logger) {
        String payload = logger.value();
        if (payload == null || payload.isEmpty()) {
            payload = logger.message();
        }
        return payload;
    }

    @Before("@annotation(RemoteLogger)")
    public void afterReturningWithAnnotation(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = (signature).getMethod();
        RemoteLogger logger = method.getAnnotation(RemoteLogger.class);
        String payload = extractPayload(logger);
        RemoteLogger.LogType logType = logger.type();
        int[] argsIndex = logger.argumentsIndex();

        Map<String, Object> arguments = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        switch (logType) {
            case ALL:
                for (var i = 0; i < joinPoint.getArgs().length; i++) {
                    arguments.put(paramNames[i], paramValues[i]);
                }
                break;
            case SELECTION:
                for (int index : argsIndex) {
                    if (index > joinPoint.getArgs().length) {
                        throw new IllegalArgumentException("Log index not valid");
                    }
                    if (index < 1) {
                        throw new IllegalArgumentException("Log index start from 1");
                    }
                    index--;
                    arguments.put(paramNames[index], paramValues[index]);
                }
                break;
            case NONE:
                break;
        }

        try {
            Logger log = LoggerFactory.getLogger(method.getDeclaringClass());
            Method loggerMethod = log.getClass()
                    .getDeclaredMethod(logger.level().name().toLowerCase(), String.class, Object[].class);

            try (var ignored = MDC.putCloseable("isAspect", "true")) {
                loggerMethod.invoke(log, payload, new Object[]{arguments});
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Logger not found");
        }
    }

    @Around("@within(com.elk.aspect.logger.RemoteLogger)")
    public Object applyAspectToMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        //TODO: Apply the same logic to class level (all inner methods)
        return joinPoint.proceed();
    }
}
