package com.annotator.application.algorithm.SPIP;

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
public class SPIPInput extends CsvBean {
    @CsvBindByName(column = "ID")
    private String id;

    @CsvBindByName(column = "#CHROM")
    private String chrom;

    @CsvBindByName(column = "POS")
    private long position;

    @CsvBindByName(column = "REF")
    private String ref;

    @CsvBindByName(column = "ALT")
    private String alt;

    public static SPIPInput from(final AnnotationRequest request) {
        final var variant = request.getVariant();
        return new SPIPInput(
                variant.getGene(),
                variant.getChromosome(),
                variant.getPosition(),
                variant.getReferenceAllele(),
                variant.getAlternativeAllele()
        );
    }
}
