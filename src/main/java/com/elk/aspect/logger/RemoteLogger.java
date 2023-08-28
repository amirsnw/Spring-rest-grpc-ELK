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

    LogLevel type() default LogLevel.INFO;

    enum LogLevel {
        ERROR, WARN, INFO, DEBUG, TRACE;
    }

}