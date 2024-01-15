package com.annotator.core.application.variantparser;

import com.annotator.core.domain.annotation.Variant;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class VariantInput {
    @CsvBindByName(column = "HGVS")
    private String gene;

    @CsvBindByName(column = "Chr")
    private String chrom;

    @CsvBindByName(column = "POS")
    private long position;

    @CsvBindByName(column = "Ref")
    private String ref;

    @CsvBindByName(column = "Alt")
    private String alt;

    public Variant toVariant() {
        return Variant.builder()
                .gene(gene)
                .alternativeAllele(alt)
                .referenceAllele(ref)
                .chromosome(chrom)
                .position(position)
                .build();
    }
}
