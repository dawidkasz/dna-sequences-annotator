package com.annotator.core.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@EqualsAndHashCode
@ToString
public class Variant {
    private final String chromosome;
    private final long position;
    private final Allele referenceAllele;
    private final Allele alternativeAllele;
    private final VariantType type;

    private Variant(String chromosome, long position, Allele referenceAllele, Allele alternativeAllele) {
        Objects.requireNonNull(chromosome);
        Objects.requireNonNull(referenceAllele);
        Objects.requireNonNull(alternativeAllele);

        this.chromosome = chromosome;
        this.position = position;
        this.referenceAllele = referenceAllele;
        this.alternativeAllele = alternativeAllele;

        boolean isSnv = referenceAllele.size() == 1 && alternativeAllele.size() == 1 &&
                !referenceAllele.isBlank() && !referenceAllele.isBlank();

        this.type = isSnv ? VariantType.SNV : VariantType.INDEL;
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

    public static class Builder {
        private String chromosome;
        private long position;
        private Allele referenceAllele;
        private Allele alternativeAllele;

        public Builder chromosome(String chromosome) {
            this.chromosome = chromosome;
            return this;
        }

        public Builder position(long position) {
            this.position = position;
            return this;
        }

        public Builder referenceAllele(Allele allele) {
            this.referenceAllele = allele;
            return this;
        }

        public Builder referenceAllele(String allele) {
            return referenceAllele(Allele.from(allele));
        }

        public Builder alternativeAllele(Allele allele) {
            this.alternativeAllele = allele;
            return this;
        }

        public Builder alternativeAllele(String allele) {
            return alternativeAllele(Allele.from(allele));
        }

        public Variant build() {
            return new Variant(chromosome, position, referenceAllele, alternativeAllele);
        }
    }
}
