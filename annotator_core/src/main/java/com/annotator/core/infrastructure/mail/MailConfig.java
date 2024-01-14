package com.annotator.core.infrastructure.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Bean
    @ConditionalOnProperty(prefix = "spring.notify.mail", name = "enabled", havingValue = "true")
    public JavaMailSender javaMailSender(@Value("${spring.mail.host}") final String host,
                                         @Value("${spring.mail.port}") final int port,
                                         @Value("${spring.mail.username}") final String username,
                                         @Value("${spring.mail.password}") final String password) {
        final var sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setPort(port);
        return sender;
    }
}
