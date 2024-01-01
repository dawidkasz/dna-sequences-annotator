package com.annotator.core.domain.order.result;

import com.annotator.core.domain.annotation.*;
import com.annotator.core.domain.order.OrderId;
import com.annotator.core.domain.order.request.AnnotationRequestId;

import java.util.List;

public record AnnotationResult(AnnotationRequestId annotationRequestId, OrderId orderId,
                               AnnotationId annotationId, AnnotationAlgorithm algorithm, String result) {

    public VariantAnnotations toAnnotation(final Variant variant) {
        return new VariantAnnotations(annotationId, variant, List.of(new Annotation(algorithm, result)));
    }
}
