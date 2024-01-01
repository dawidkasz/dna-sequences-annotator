package com.annotator.core.infrastructure.kafka;

import com.annotator.core.domain.order.result.AnnotationResultConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAnnotationResultConsumer {
    private final AnnotationResultConsumer annotationConsumer;

    @KafkaListener(topics = "${spring.kafka.annotation-result-topic}")
    public void listen(final AnnotatedResult result) {
        annotationConsumer.consume(result.toDomain());
    }
}
