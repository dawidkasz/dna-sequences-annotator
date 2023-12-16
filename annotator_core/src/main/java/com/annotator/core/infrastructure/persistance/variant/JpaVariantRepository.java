package com.annotator.core.infrastructure.persistance.variant;

import com.annotator.core.application.VariantRepository;
import com.annotator.core.domain.Allele;
import com.annotator.core.domain.Variant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaVariantRepository implements VariantRepository {
    private final SpringDataVariantRepository repository;

    @Override
    public void save(Variant variant) {
        repository.save(JpaVariant.from(variant));
    }

    @Override
    public Optional<Variant> find(String chromosome, long position, Allele refAllele, Allele altAllele) {
        return repository.findByChromosomeAndPositionAndReferenceAlleleAndAlternativeAllele(
            chromosome, position, refAllele.toString(), altAllele.toString()
        ).map(JpaVariant::toVariant);
    }
}
