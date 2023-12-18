package com.annotator.core.application;

import com.annotator.core.domain.Annotation;

public interface AnnotationConsumer {
    void consumeAnnotation(Annotation annotation);
}
