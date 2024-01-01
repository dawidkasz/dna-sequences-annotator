package com.annotator.core.domain.annotation;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.annotation.Nullable;

@Data
@Builder
public class Variant {
    private final String chromosome;
    private final long position;
    private final Allele referenceAllele;
    private final Allele alternativeAllele;
    private final VariantType type;
    private final String gene;

    private Variant(final @NonNull String chromosome, final long position, final @NonNull Allele referenceAllele, final @NonNull Allele alternativeAllele, @Nullable final String gene) {
        super();
        this.chromosome = chromosome;
        this.position = position;
        this.referenceAllele = referenceAllele;
        this.alternativeAllele = alternativeAllele;

        final boolean isSnv = referenceAllele.size() == 1 && alternativeAllele.size() == 1 &&
                !referenceAllele.isBlank() && !referenceAllele.isBlank();

        this.type = isSnv ? VariantType.SNV : VariantType.INDEL;
        this.gene = gene;
    }

    public boolean isSnv() {
        return this.type == VariantType.SNV;
    }

    public boolean isIndel() {
        return this.type == VariantType.INDEL;
    }

    public enum VariantType {
        SNV,
        INDEL
    }

    public static class VariantBuilder {
        public VariantBuilder referenceAllele(final String allele) {
            this.referenceAllele = Allele.from(allele);
            return this;
        }

        public VariantBuilder alternativeAllele(final String allele) {
            this.alternativeAllele = Allele.from(allele);
            return this;
        }

        public Variant build() {
            return new Variant(chromosome, position, referenceAllele, alternativeAllele, gene);
        }
    }
}
