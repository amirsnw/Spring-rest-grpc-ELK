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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.v;

@Component
@Aspect
public class RemoteLoggerAspect {

    @Before("@annotation(RemoteLogger)")
    public void afterReturningWithAnnotation(JoinPoint joinPoint)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = (signature).getMethod();
        RemoteLogger logger = method.getAnnotation(RemoteLogger.class);
        String payload = logger.value();
        Logger log = LoggerFactory.getLogger(method.getDeclaringClass());
        Method loggerMethod = log.getClass()
                .getDeclaredMethod(logger.type().name().toLowerCase(), String.class, Object[].class);

        List<Object> keyValueList = new ArrayList<>();
        Iterator<Object> paramValues = Arrays.stream(joinPoint.getArgs()).iterator();
        Iterator<String> paramNames = Arrays.stream(signature.getParameterNames()).iterator();
        while (paramValues.hasNext()) {
            keyValueList.add(v(paramNames.next(), paramValues.next()));
        }

        loggerMethod.invoke(log, payload, keyValueList.toArray());
    }

    @Around("@within(com.elk.aspect.logger.RemoteLogger)")
    public Object applyAspectToMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        //TODO: Apply the same logic to class level (all inner methods)
        return joinPoint.proceed();
    }

}
