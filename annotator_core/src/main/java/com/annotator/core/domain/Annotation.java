package com.annotator.core.domain;

public record Annotation(
        AnnotationId annotationId,
        Variant variant,
        AnnotationAlgorithm algorithm,
        String result
) {
}
