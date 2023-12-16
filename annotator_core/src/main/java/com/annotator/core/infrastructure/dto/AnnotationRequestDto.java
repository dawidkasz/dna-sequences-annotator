package com.annotator.core.infrastructure.dto;

import com.annotator.core.domain.AnnotationAlgorithm;
import com.annotator.core.domain.AnnotationId;
import com.annotator.core.domain.Variant;

public record AnnotationRequestDto(
        AnnotationId annotationId,
        Variant variant,
        AnnotationAlgorithm algorithm
) {
}