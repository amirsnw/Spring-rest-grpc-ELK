package com.elk.config.web;

import brave.Tracing;
import brave.propagation.B3Propagation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(value = "tracing.enableMultiB3", havingValue = "true")
@Configuration
public class BraveConfiguration {

    @Bean
    public Tracing braveTracing() {
        return Tracing.newBuilder()
                .propagationFactory(B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build())
                .build();
    }
}
