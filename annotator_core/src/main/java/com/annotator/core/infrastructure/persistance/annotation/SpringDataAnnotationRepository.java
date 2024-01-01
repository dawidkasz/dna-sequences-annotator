package com.annotator.core.infrastructure.persistance.annotation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataAnnotationRepository extends CrudRepository<JpaAnnotation, JpaVariant> {
    Optional<JpaAnnotation> findByAnnotationId(UUID annotationId);
}
