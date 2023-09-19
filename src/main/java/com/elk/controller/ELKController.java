package com.elk.controller;

import com.elk.aspect.logger.RemoteLogger;
import com.elk.model.WelcomePackage;
import com.elk.service.HelloWorldService;
import com.elk.service.grpc.client.HelloWorldGrpcClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class ELKController {

    private final HelloWorldService helloWorldService;

    private final HelloWorldGrpcClient grpcClient;

    public ELKController(HelloWorldService helloWorldService, HelloWorldGrpcClient grpcClient) {
        this.helloWorldService = helloWorldService;
        this.grpcClient = grpcClient;
    }

    @GetMapping("/hello/{name}")
    @RemoteLogger(message = "/hello endpoint called",
            type = RemoteLogger.LogType.SELECTION,
            argumentsIndex = {1})
    public ResponseEntity<String> sayHello(@PathVariable("name") String name) {
        return ResponseEntity.ok(helloWorldService
                .sayHello(new WelcomePackage(name, "Hello"), "my friend"));
    }

    @GetMapping("/hello-grpc")
    @RemoteLogger("/hello-grpc endpoint called")
    public ResponseEntity<String> sayHelloGrpc() {
        return ResponseEntity.ok(grpcClient.callNotification("Sample Question"));
    }

    @GetMapping("/bad-call")
    @RemoteLogger("/other endpoint called")
    public void badCall() {
        log.info("Someone called the /bad-call endpoint");
        helloWorldService.fakeBadCall();
    }
}
