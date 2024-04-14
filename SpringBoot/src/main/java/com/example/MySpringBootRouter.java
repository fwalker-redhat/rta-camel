package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {

        from("timer:hello?period={{timer.period}}").routeId("hello")
            .to("https://{{rest.url}}")
            .marshal().base64()
            .to("kafka:test-topic?brokers={{kafka.brokers}}");
            
    }

}
