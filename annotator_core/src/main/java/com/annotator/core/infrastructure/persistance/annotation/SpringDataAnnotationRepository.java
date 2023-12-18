package com.annotator.core.infrastructure.persistance.annotation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataAnnotationRepository extends CrudRepository<JpaAnnotation, Long> {
    Optional<List<JpaAnnotation>> findAllByBatchId(String batchId);
}
