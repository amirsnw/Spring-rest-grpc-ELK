package com.elk.service;

import com.elk.aspect.logger.RemoteLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.elk.config.CommonConst.GRPC_ADDRESS;

@Service
@Slf4j
public class HelloWorldService {

    private final RestTemplate restTemplate;

    public HelloWorldService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RemoteLogger("sayHello() method is called")
    public String sayHello(String words) {
        restTemplate.getForEntity(GRPC_ADDRESS, String.class);
        return "hello";
    }

    @RemoteLogger("fakeBadCall() method is called")
    public void fakeBadCall() {
        throw new IllegalArgumentException("Exception from Hello World Service");
    }
}
