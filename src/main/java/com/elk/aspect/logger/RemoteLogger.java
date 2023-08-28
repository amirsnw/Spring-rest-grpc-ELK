package com.elk.aspect.logger;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RemoteLogger {

    String value() default "";

    LogLevel type() default LogLevel.INFO;

    enum LogLevel {
        ERROR, WARN, INFO, DEBUG, TRACE;
    }

}