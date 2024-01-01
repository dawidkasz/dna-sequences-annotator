package com.annotator.core.domain.annotation;

import com.annotator.core.domain.order.result.AnnotationResult;

import java.util.Optional;

public interface VariantsAnnotationsRepository {
    boolean exists(Variant variant);

    void save(VariantAnnotations annotations);

    void save(AnnotationResult result);

    Optional<VariantAnnotations> findById(AnnotationId annotationId);

    Optional<VariantAnnotations> findByVariant(Variant variant);
}
