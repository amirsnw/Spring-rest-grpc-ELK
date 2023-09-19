package com.elk.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

import java.util.Map;

public class ArgumentsConverter extends ClassicConverter {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    @SneakyThrows
    public String convert(ILoggingEvent event) {
        Object[] arguments = event.getArgumentArray();

        if (arguments != null && arguments.length > 0) {
            for (Object argument : arguments) {
                return switch (argument) {
                    case Map<?, ?> m -> mapper.writeValueAsString(m);
                    case String s -> mapper.writeValueAsString(s.split(", "));
                    default -> "";
                };
            }
        }

        return "";
    }
}