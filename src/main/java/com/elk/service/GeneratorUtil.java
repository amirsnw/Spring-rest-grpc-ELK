package com.elk.service;

import com.fasterxml.uuid.Generators;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class GeneratorUtil {

    public static final String UUID_STRING =
            "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";

    public String traceIdGenerator() {
        return Generators.timeBasedGenerator().generate().toString().replace("-", "");
    }

    public String spanIdGenerator() {
        return Generators.timeBasedGenerator().generate().toString().replace("-", "").substring(16);
    }

    public String getAsSpanId(String traceId) {
        Pattern UUID = Pattern.compile(UUID_STRING);
        if (UUID.matcher(traceId).matches()) {
            throw new IllegalArgumentException("invalid universal UUID");
        }
        return traceId.substring(16);
    }
}
