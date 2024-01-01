package com.annotator.core.infrastructure.kafka;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.AnnotationId;
import com.annotator.core.domain.order.OrderId;
import com.annotator.core.domain.order.request.AnnotationRequestId;
import com.annotator.core.domain.order.result.AnnotationResult;

import java.util.UUID;

public record AnnotatedResult(
        UUID annotationRequestId, UUID orderId,
        UUID annotationId, String algorithm, String result) {
    public AnnotationResult toDomain() {
        return new AnnotationResult(AnnotationRequestId.from(annotationRequestId), new OrderId(orderId),
                new AnnotationId(annotationId), AnnotationAlgorithm.valueOf(algorithm), result);
    }

}
