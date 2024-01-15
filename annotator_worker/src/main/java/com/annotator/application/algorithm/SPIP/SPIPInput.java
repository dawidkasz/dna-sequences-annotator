package com.annotator.application.algorithm.SPIP;

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
public class SPIPInput extends CsvBean {
    @CsvBindByName(column = "gene")
    private String id;

    @CsvBindByName(column = "varID")
    private String chrom;

    public static SPIPInput from(final AnnotationRequest request) {
        final var variant = request.getVariant();
        return new SPIPInput(
                variant.getChromosome(),
                variant.getGene().split(" ")[0]
        );
    }

    public static List<SPIPInput> from(final List<AnnotationRequest> requests) {
        return requests.stream()
                .map(SPIPInput::from)
                .collect(Collectors.toList());
    }
}
