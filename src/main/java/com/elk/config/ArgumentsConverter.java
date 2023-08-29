package com.elk.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.Map;

public class ArgumentsConverter extends ClassicConverter {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public String convert(ILoggingEvent event) {
        Object[] arguments = event.getArgumentArray();

        if (arguments[0] instanceof Map<?, ?>) {
            return mapper.writeValueAsString(arguments[0].toString());
            // return arguments[0].toString();
        }

        return "";
    }
}