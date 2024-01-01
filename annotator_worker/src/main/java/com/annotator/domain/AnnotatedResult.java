package com.annotator.domain;

import java.util.UUID;

public record AnnotatedResult(
        UUID annotationRequestId, UUID orderId,
        UUID annotationId, String algorithm, String result) {

}
