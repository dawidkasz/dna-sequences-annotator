package com.annotator.core.domain.annotation;

import com.annotator.core.domain.order.result.AnnotationResult;

import java.util.List;
import java.util.Optional;

public interface VariantsAnnotationsRepository {
    boolean exists(Variant variant);

    boolean isAnnotated(Variant variant, List<AnnotationAlgorithm> algorithms);

    void save(VariantAnnotations annotations);

    void save(AnnotationResult result);

    Optional<VariantAnnotations> findById(AnnotationId annotationId);

    Optional<VariantAnnotations> findByVariant(Variant variant);

    List<VariantAnnotations> findWithMissingAlgorithms(List<Variant> variants, List<AnnotationAlgorithm> algorithms);
}
