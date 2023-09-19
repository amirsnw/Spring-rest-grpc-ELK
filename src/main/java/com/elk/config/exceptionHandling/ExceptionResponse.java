package com.elk.config.exceptionHandling;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {

    private final String traceId;

    private final String spanId;

    private final HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime datetime;

    private final String error;

    ExceptionResponse(@NonNull HttpStatus status, @NonNull Exception ex, String traceId, String spanId) {
        datetime = LocalDateTime.now();
        this.status = status;
        this.error = ex.getMessage();
        this.traceId = traceId;
        this.spanId = spanId;
    }
}
