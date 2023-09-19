package com.elk.config.exceptionHandling;

import com.elk.config.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class HttpExceptionHandler {

    private final ContextHolder contextHolder;

    public HttpExceptionHandler(ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.warn(exception.getMessage(), exception);
        var exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, contextHolder.getTraceId(), contextHolder.getSpanId());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
    }
}
