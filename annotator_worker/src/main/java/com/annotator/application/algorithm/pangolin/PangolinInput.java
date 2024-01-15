package com.annotator.application.algorithm.pangolin;

import com.annotator.domain.AnnotationRequest;
import com.annotator.infra.CsvBean;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PangolinInput extends CsvBean {
    @CsvBindByName(column = "gene")
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
                "aaa",
                variant.getChromosome(),
                variant.getPosition(),
                variant.getReferenceAllele(),
                variant.getAlternativeAllele()
        );
    }

    public static List<PangolinInput> from(final List<AnnotationRequest> requests) {
        return requests.stream()
                .map(PangolinInput::from)
                .collect(Collectors.toList());
    }
}
