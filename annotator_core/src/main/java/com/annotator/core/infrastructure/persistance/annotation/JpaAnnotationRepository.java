package com.annotator.core.infrastructure.persistance.annotation;

import com.annotator.core.domain.annotation.*;
import com.annotator.core.domain.order.result.AnnotationResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaAnnotationRepository implements VariantsAnnotationsRepository {
    private final SpringDataAnnotationRepository annotationRepository;
    private final JpaVariantRepository variantRepository;

    private static JpaAnnotation toJpa(final VariantAnnotations annotation) {
        final var results = annotation.annotations().stream().collect(Collectors.toMap(x -> x.algorithm().name(), Annotation::result));
        return JpaAnnotation.builder()
                .variant(JpaVariantDetails.from(annotation.variant()))
                .results(results)
                .annotationId(annotation.annotationId().uuid())
                .annotated(annotation.annotations().size() == AnnotationAlgorithm.values().length)
                .build();
    }


    @Override
    public boolean exists(final Variant variant) {
        return annotationRepository.existsById(JpaVariantDetails.from(variant));
    }

    @Override
    public boolean isAnnotated(final Variant variant) {
        return annotationRepository.findById(JpaVariantDetails.from(variant))
                .map(JpaAnnotation::isAnnotated)
                .orElse(false);
    }

    @Override
    @Transactional
    public void save(final VariantAnnotations annotation) {
        final var jpaAnnotation = JpaAnnotationRepository.toJpa(annotation);
        annotationRepository
                .findById(jpaAnnotation.getVariant())
                .ifPresentOrElse(
                        result -> {
                            //TODO use native postgres hstack
                            annotation.annotations().forEach(x -> result.getResults().put(x.algorithm().name(), x.result()));
                            result.setAnnotated(result.getResults().size() == AnnotationAlgorithm.values().length);
                            annotationRepository.save(result);
                        },
                        () -> {
                            final var variantId = variantRepository.findIdOrCreate(jpaAnnotation.getVariant());
                            jpaAnnotation.setVariantId(variantId);
                            annotationRepository.save(jpaAnnotation);
                        });


    }

    @Override
    @Transactional
    public void save(final AnnotationResult result) {
        annotationRepository.findByAnnotationId(result.annotationId().uuid())
                .ifPresent(jpaAnnotation -> {
                    jpaAnnotation.getResults().put(result.algorithm().name(), result.result());
                    jpaAnnotation.setAnnotated(jpaAnnotation.getResults().size() == AnnotationAlgorithm.values().length);
                    annotationRepository.save(jpaAnnotation);
                });
    }

    @Override
    public Optional<VariantAnnotations> findById(final AnnotationId annotationId) {
        return annotationRepository.findByAnnotationId(annotationId.uuid()).map(JpaAnnotation::toAnnotation);
    }

    @Override
    public Optional<VariantAnnotations> findByVariant(final Variant variant) {
        return annotationRepository.findById(JpaVariantDetails.from(variant)).map(JpaAnnotation::toAnnotation);
    }

    public List<JpaAnnotation> findAllByVariantsId(final List<Long> variantIds) {
        return annotationRepository.findAllByVariantIdIn(variantIds);
    }
}
