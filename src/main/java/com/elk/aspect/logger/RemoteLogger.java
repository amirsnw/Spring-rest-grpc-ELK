package com.elk.aspect.logger;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RemoteLogger {

    @AliasFor("message")
    String value() default "";

    @AliasFor("value")
    String message() default "";

    LogLevel level() default LogLevel.INFO;

    LogType type() default LogType.NONE;

    int[] argumentsIndex() default {};

    enum LogLevel {
        ERROR, WARN, INFO, DEBUG, TRACE;
    }

    enum LogType {
        ALL, NONE, SELECTION;
    }

}