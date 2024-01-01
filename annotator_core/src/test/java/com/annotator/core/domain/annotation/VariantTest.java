package com.annotator.core.domain.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VariantTest {
    @Test
    public void shouldBuild_whenAllelesAsStringPassed() {
        //given
        final var variant = Variant.builder()
                .referenceAllele("A")
                .alternativeAllele("T")
                .chromosome("chrom")
                .build();

        //when
        final var reference = variant.getReferenceAllele();
        final var alternative = variant.getAlternativeAllele();

        //then
        Assertions.assertEquals(reference.toString(), Allele.from("A").toString());
        Assertions.assertEquals(alternative.toString(), Allele.from("T").toString());
    }

}