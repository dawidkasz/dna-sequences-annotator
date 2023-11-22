package com.annotator.core.repository;

import com.annotator.core.domain.AnnotationResult;
import org.springframework.data.repository.CrudRepository;

public interface AnnotationsRepository extends CrudRepository<AnnotationResult, Long> {
}
