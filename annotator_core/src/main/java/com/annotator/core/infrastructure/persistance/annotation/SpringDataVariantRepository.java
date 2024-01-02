package com.annotator.core.infrastructure.persistance.annotation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataVariantRepository extends CrudRepository<JpaVariant, Long> {
    Optional<JpaVariant> findByVariant(JpaVariantDetails variant);

    @Query("SELECT v.id FROM JpaVariant v WHERE v.variant = :variant")
    Optional<Long> findIdByVariant(@Param("variant") JpaVariantDetails variant);


    @Query("SELECT v.id FROM JpaVariant v WHERE v.variant in :variants")
    List<Long> findAllIdByVariantIn(@Param("variants") List<JpaVariantDetails> variants);
}
