package com.annotator.core.infrastructure.csv;

import com.annotator.core.domain.annotation.VariantAnnotations;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AnnotationResultsCsv extends CsvBean {
    @CsvBindByName(column = "GENE")
    private String gene;

    @CsvBindByName(column = "CHROM")
    private String chrom;

    @CsvBindByName(column = "POS")
    private long position;

    @CsvBindByName(column = "REF")
    private String ref;

    @CsvBindByName(column = "ALT")
    private String alt;

    @CsvBindByName(column = "RESULT")
    private String result;

    public static AnnotationResultsCsv from(final VariantAnnotations annotations) {
        final var variant = annotations.variant();
        return new AnnotationResultsCsv(
                variant.getGene(),
                variant.getChromosome(),
                variant.getPosition(),
                variant.getReferenceAllele().toString(),
                variant.getAlternativeAllele().toString(),
                annotations.annotations().toString()
        );
    }
}
