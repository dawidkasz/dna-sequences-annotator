package com.annotator.core.infrastructure.persistance.order;

import com.annotator.core.infrastructure.persistance.annotation.JpaVariant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataVariantRepository extends CrudRepository<JpaOrderVariant, Long> {
    Optional<JpaOrderVariant> findByVariant(JpaVariant variant);
}
