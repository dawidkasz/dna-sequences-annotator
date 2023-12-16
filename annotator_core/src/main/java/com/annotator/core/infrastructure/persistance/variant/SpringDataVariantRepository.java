package com.annotator.core.infrastructure.persistance.variant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataVariantRepository extends CrudRepository<JpaVariant, Long> {
    Optional<JpaVariant> findByChromosomeAndPositionAndReferenceAlleleAndAlternativeAllele(
        String chromosome, long position, String referenceAllele, String alternativeAllele
    );
}
