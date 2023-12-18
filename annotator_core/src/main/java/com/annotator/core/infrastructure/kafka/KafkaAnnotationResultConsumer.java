package com.annotator.core.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAnnotationResultConsumer {
    @KafkaListener(topics = "${spring.kafka.annotation-result-topic}")
    public void listen(AnnotatedResult result) {
        log.info("Received: {}", result);
    }
}
