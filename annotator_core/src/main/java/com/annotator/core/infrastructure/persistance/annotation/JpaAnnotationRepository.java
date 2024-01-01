package com.annotator.core.infrastructure.persistance.annotation;

import com.annotator.core.domain.annotation.*;
import com.annotator.core.domain.order.result.AnnotationResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaAnnotationRepository implements VariantsAnnotationsRepository {
    private final SpringDataAnnotationRepository annotationRepository;

    private static JpaAnnotation toJpa(final VariantAnnotations annotation) {
        final var results = annotation.annotations().stream().collect(Collectors.toMap(x -> x.algorithm().name(), Annotation::result));
        return JpaAnnotation.builder()
                .variant(JpaVariant.from(annotation.variant()))
                .results(results)
                .annotationId(annotation.annotationId().uuid())
                .state(!annotation.annotations().isEmpty())
                .build();
    }


    @Override
    public boolean exists(final Variant variant) {
        return annotationRepository.existsById(JpaVariant.from(variant));
    }

    @Override
    @Transactional
    public void save(final VariantAnnotations annotation) {
        final var jpaAnnotation = JpaAnnotationRepository.toJpa(annotation);
        annotationRepository
                .findById(jpaAnnotation.getVariant())
                .ifPresentOrElse(
                        result -> {
                            annotation.annotations().forEach(x -> result.getResults().put(x.algorithm().name(), x.result()));
                            result.setState(!result.getResults().isEmpty());
                            annotationRepository.save(result);
                        }, () -> annotationRepository.save(jpaAnnotation)
                );

    }

    @Override
    @Transactional
    public void save(final AnnotationResult result) {
        annotationRepository.findByAnnotationId(result.annotationId().uuid())
                .ifPresent(jpaAnnotation -> {
                    jpaAnnotation.getResults().put(result.algorithm().name(), result.result());
                    jpaAnnotation.setState(true);
                    annotationRepository.save(jpaAnnotation);
                });
    }

    @Override
    public Optional<VariantAnnotations> findById(final AnnotationId annotationId) {
        return annotationRepository.findByAnnotationId(annotationId.uuid()).map(JpaAnnotation::toAnnotation);
    }

    @Override
    public Optional<VariantAnnotations> findByVariant(final Variant variant) {
        return annotationRepository.findById(JpaVariant.from(variant)).map(JpaAnnotation::toAnnotation);
    }
}
