package com.annotator.core.infrastructure.kafka;

import com.annotator.core.application.AnnotationConsumer;
import com.annotator.core.domain.Annotation;
import com.annotator.core.domain.AnnotationAlgorithm;
import com.annotator.core.domain.AnnotationId;
import com.annotator.core.infrastructure.persistance.variant.SpringDataVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAnnotationResultConsumer {
    private final AnnotationConsumer annotationConsumer;
    private final SpringDataVariantRepository variantRepository;

    @KafkaListener(topics = "${spring.kafka.annotation-result-topic}")
    public void listen(final AnnotatedResult result) {
        variantRepository
                .findByChromosomeAndPositionAndReferenceAlleleAndAlternativeAllele(result.getChromosome(), result.getPosition(),
                        result.getReferenceAllele(), result.getAlternativeAllele())
                .ifPresent(jpaVariant -> {
                    final var algorithm = AnnotationAlgorithm.valueOf(result.getAlgorithm());
                    final var id = AnnotationId.from(result.getAnnotationId().getId());
                    final var annotation = new Annotation(id, jpaVariant.toVariant(), algorithm, result.getResult());
                    annotationConsumer.consumeAnnotation(annotation);
                });
    }
}
