package com.annotator.core.infrastructure.web;

import com.annotator.core.domain.annotation.VariantAnnotations;
import com.annotator.core.infrastructure.web.annotations.JSONAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "spring.notify.webhook", name = "enabled", havingValue = "true")
public class AnnotationResultsClient {
    private final RestTemplate restTemplate;

    public void sendResults(final List<VariantAnnotations> annotations, final URI url) {
        final var data = annotations.stream().map(JSONAnnotation::from).toList();
        try {
            final var response = restTemplate.postForEntity(url, data, Void.class);
            if (response.getStatusCode().isError()) {
                log.warn("There was error when sending webhook notification, HTTP: {}", response.getStatusCode());
            }
        } catch (final RestClientException e) {
            log.warn("There was error when sending webhook notification: {}", e.getMessage());
        }
    }
}
