package com.annotator.core.domain;

import java.util.List;

public record Batch(
        AnnotationId annotationId,
        List<Annotation> annotations
) {
}
