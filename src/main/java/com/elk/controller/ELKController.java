package com.elk.controller;

import com.elk.aspect.logger.RemoteLogger;
import com.elk.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class ELKController {

    private final HelloWorldService helloWorldService;

    public ELKController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping("/hello")
    @RemoteLogger("Someone called the /hello endpoint")
    public ResponseEntity<String> sayHello() {
        // log.info("Someone called the /hello endpoint");
        return ResponseEntity.ok(/*helloWorldService.sayHello()*/"");
    }

    @GetMapping("/bad-call")
    public void badCall() {
        log.info("Someone called the /bad-call endpoint");
        helloWorldService.fakeBadCall();
    }
}
