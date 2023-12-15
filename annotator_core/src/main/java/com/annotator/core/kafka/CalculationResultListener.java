package com.annotator.core.kafka;

import com.annotator.core.domain.AnnotationResult;
import com.annotator.core.domain.CalculationResult;
import com.annotator.core.repository.AnnotationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CalculationResultListener {
    private final AnnotationsRepository annotationRepository;

    @KafkaListener(topics = "demo_pzsp2_result", groupId = "core-annotator")
    public void listen(CalculationResult result) {
        log.warn("Received: {}", result);
        annotationRepository.save(AnnotationResult.from(result));
    }
}
