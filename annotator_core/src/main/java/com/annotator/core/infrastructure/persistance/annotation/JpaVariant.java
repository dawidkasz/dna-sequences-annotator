package com.annotator.core.infrastructure.persistance.annotation;


import com.annotator.core.domain.annotation.Variant;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaVariant implements Serializable {
    @Column
    private String chromosome;
    @Column
    private long position;
    @Column
    private String referenceAllele;
    @Column
    private String alternativeAllele;
    @Column
    private Variant.VariantType type;
    @Column
    private String gene;

    public static JpaVariant from(final Variant variant) {
        return JpaVariant.builder()
                .chromosome(variant.getChromosome())
                .position(variant.getPosition())
                .referenceAllele(variant.getReferenceAllele().toString())
                .alternativeAllele(variant.getAlternativeAllele().toString())
                .type(variant.getType())
                .gene(variant.getGene())
                .build();
    }

    public Variant toVariant() {
        return Variant.builder()
                .chromosome(chromosome)
                .position(position)
                .referenceAllele(referenceAllele)
                .alternativeAllele(alternativeAllele)
                .gene(gene)
                .build();
    }
}