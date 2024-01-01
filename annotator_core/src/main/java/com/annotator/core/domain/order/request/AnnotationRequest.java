package com.annotator.core.domain.order.request;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.AnnotationId;
import com.annotator.core.domain.annotation.Variant;
import com.annotator.core.domain.order.OrderId;

public record AnnotationRequest(
        AnnotationRequestId annotationRequestId,
        OrderId orderId,
        AnnotationId annotationId,
        Variant variant,
        AnnotationAlgorithm algorithm
) {

    public static AnnotationRequest from(final OrderId orderId, final AnnotationId annotationId, final Variant variant, final AnnotationAlgorithm algorithm) {
        return new AnnotationRequest(AnnotationRequestId.create(), orderId, annotationId, variant, algorithm);
    }
}
