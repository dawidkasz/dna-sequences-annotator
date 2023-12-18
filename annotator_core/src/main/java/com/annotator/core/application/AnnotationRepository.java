package com.annotator.core.application;

import com.annotator.core.domain.Annotation;

import java.util.List;

public interface AnnotationRepository {

    void save(Annotation annotation);

    List<Annotation> getBatch(String batchId);
}
