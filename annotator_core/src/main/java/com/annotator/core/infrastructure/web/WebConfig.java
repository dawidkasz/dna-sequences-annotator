package com.annotator.core.infrastructure.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.notify.webhook", name = "enabled", havingValue = "true")
    public RestTemplate restClient() {
        return new RestTemplate();
    }
}
