package com.annotator.core.infrastructure.persistance.annotation;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaVariantRepository {
    private final SpringDataVariantRepository variantRepository;

    public JpaVariant findOrCreateVariant(final JpaVariantDetails variant) {
        return variantRepository
                .findByVariant(variant)
                .orElseGet(() -> variantRepository.save(new JpaVariant(variant)));
    }

    public List<JpaVariant> findAllByVariants(final List<Long> ids) {
        return Lists.newArrayList(variantRepository.findAllById(ids));
    }

    public Long findIdOrCreate(final JpaVariantDetails variantDetails) {
        return variantRepository.findIdByVariant(variantDetails)
                .orElseGet(() -> variantRepository.save(new JpaVariant(variantDetails)).getId());
    }

    public List<Long> findAllIds(final List<JpaVariantDetails> variants) {
        return variantRepository.findAllIdByVariantIn(variants);
    }
}
