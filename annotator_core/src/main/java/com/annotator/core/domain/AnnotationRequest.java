package com.annotator.core.domain;

public record AnnotationRequest(
    AnnotationId annotationId,
    Variant variant,
    AnnotationAlgorithm algorithm
) {
}
