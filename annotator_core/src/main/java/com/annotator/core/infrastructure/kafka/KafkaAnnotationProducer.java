package com.annotator.core.infrastructure.kafka;

import com.annotator.core.application.AnnotationProducer;
import com.annotator.core.domain.AnnotationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaAnnotationProducer implements AnnotationProducer {
    private final KafkaTemplate<String, AnnotationRequest> template;
    @Value("${spring.kafka.annotation-request-topic}")
    private String topic;

    @Override
    public void annotate(AnnotationRequest request) {
        log.debug("Sending variant {} annotation request using algorithm {}", request.variant(), request.algorithm());
        template.send(topic, String.valueOf(request.hashCode()), request);
    }
}
