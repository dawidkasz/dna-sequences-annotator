package com.annotator.core.infrastructure.persistance.variant;


import com.annotator.core.domain.Variant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaVariant {
    @Id
    @GeneratedValue
    Long id;

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

    public JpaVariant(String chromosome, long position, String referenceAllele, String alternativeAllele, Variant.VariantType type) {
        this.chromosome = chromosome;
        this.position = position;
        this.referenceAllele = referenceAllele;
        this.alternativeAllele = alternativeAllele;
        this.type = type;
    }

    public static JpaVariant from(Variant variant) {
        return new JpaVariant(
            variant.getChromosome(),
            variant.getPosition(),
            variant.getReferenceAllele().toString(),
            variant.getAlternativeAllele().toString(),
            variant.getType()
        );
    }

    public Variant toVariant() {
        System.out.println(chromosome + " x " + position + " x " + referenceAllele + " x " + alternativeAllele);

        return new Variant.Builder()
            .chromosome(chromosome)
            .position(position)
            .referenceAllele(referenceAllele)
            .alternativeAllele(alternativeAllele)
            .build();
    }
}