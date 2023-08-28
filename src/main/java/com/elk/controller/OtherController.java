package com.elk.controller;

import com.elk.aspect.logger.RemoteLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping
@Slf4j
public class OtherController {

    @GetMapping("/other")
    @RemoteLogger("/other endpoint called")
    public ResponseEntity<String> sayHello(HttpServletRequest request) {
        var headers = StreamSupport.stream(Spliterators.spliteratorUnknownSize(request.getHeaderNames().asIterator(), Spliterator.ORDERED), false).collect(Collectors.joining(", "));
        log.info("headers: {}", headers);
        log.info("traceparent: {}", request.getHeader("traceparent"));
        return ResponseEntity.ok("other");
    }
}