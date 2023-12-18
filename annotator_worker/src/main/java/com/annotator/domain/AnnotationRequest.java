package com.annotator.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class AnnotationRequest {
    private AnnotationId annotationId;
    private Variant variant;
    private AnnotationAlgorithm algorithm;

}

