package com.jjara.cloud.gateway.router;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {

    @Value("${gateway.post.uri}") private String postUri;

    @Bean
    RouteLocator route(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/post/**").uri(postUri))
                .build();
    }

}
