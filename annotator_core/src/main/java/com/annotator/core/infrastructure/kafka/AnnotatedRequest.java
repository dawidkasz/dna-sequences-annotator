package com.annotator.core.infrastructure.kafka;

import com.annotator.core.domain.annotation.Variant;
import com.annotator.core.domain.order.request.AnnotationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class AnnotatedRequest {
    private UUID annotationRequestId;
    private UUID orderId;
    private UUID annotationId;
    private Variant variant;
    private String algorithm;

    public static AnnotatedRequest from(final AnnotationRequest request) {
        return new AnnotatedRequest(request.annotationRequestId().getUuid(), request.orderId().uuid(),
                request.annotationId().uuid(), request.variant(), request.algorithm().name());
    }
}
