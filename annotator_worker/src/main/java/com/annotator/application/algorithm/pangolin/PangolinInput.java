package com.annotator.application.algorithm.pangolin;

import com.annotator.domain.AnnotationRequest;
import com.annotator.infra.CsvBean;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PangolinInput extends CsvBean {
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

    public static PangolinInput from(final AnnotationRequest request) {
        final var variant = request.getVariant();
        return new PangolinInput(
                variant.getGene(),
                variant.getChromosome(),
                variant.getPosition(),
                variant.getReferenceAllele(),
                variant.getAlternativeAllele()
        );
    }
}
