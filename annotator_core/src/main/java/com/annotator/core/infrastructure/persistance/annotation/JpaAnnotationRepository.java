package com.annotator.core.infrastructure.persistance.annotation;

import com.annotator.core.application.AnnotationRepository;
import com.annotator.core.domain.Annotation;
import com.annotator.core.infrastructure.persistance.variant.JpaVariant;
import com.annotator.core.infrastructure.persistance.variant.SpringDataVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaAnnotationRepository implements AnnotationRepository {
    private final SpringDataAnnotationRepository annotationRepository;
    private final SpringDataVariantRepository variantRepository;

    private static JpaAnnotation toJpa(final Annotation annotation, final Long variantId) {
        return JpaAnnotation.builder()
                .batchId(annotation.annotationId().toString())
                .result(annotation.result())
                .variantId(variantId)
                .algorithm(annotation.algorithm().name())
                .build();
    }

    @Override
    public void save(final Annotation annotation) {
        final var variant = annotation.variant();
        final var variantId = variantRepository.findByChromosomeAndPositionAndReferenceAlleleAndAlternativeAllele(
                variant.getChromosome(), variant.getPosition(), variant.getReferenceAllele().toString(), variant.getAlternativeAllele().toString()
        ).map(JpaVariant::getId).orElseThrow();
        annotationRepository.save(JpaAnnotationRepository.toJpa(annotation, variantId));
    }

    @Override
    public List<Annotation> getBatch(final String batchId) {
        return List.of();
    }
}
