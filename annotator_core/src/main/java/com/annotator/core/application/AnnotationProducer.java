package com.annotator.core.application;

import com.annotator.core.domain.AnnotationRequest;

public interface AnnotationProducer {
    void annotate(AnnotationRequest request);
}
