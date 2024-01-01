package com.annotator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AnnotationRequest {
    private UUID annotationRequestId;
    private UUID orderId;
    private UUID annotationId;
    private Variant variant;
    private AnnotationAlgorithm algorithm;

}

