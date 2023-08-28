package com.elk.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ArgumentsConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        Object[] arguments = event.getArgumentArray();

        if (arguments != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("[");

            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                if (!argument.toString().contains(":")) {
                    continue;
                }
                builder.append("\"").append(argument).append("\"");

                if (i < arguments.length - 1) {
                    builder.append(",");
                }
            }

            builder.append("]");
            return builder.toString();
        }

        return "[]";
    }
}