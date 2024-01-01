package com.annotator.core.infrastructure.web.annotations;

import com.annotator.core.domain.annotation.Annotation;
import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.Variant;
import com.annotator.core.domain.annotation.VariantAnnotations;

import java.util.Map;
import java.util.stream.Collectors;

public record JSONAnnotation(JSONVariant variant, Map<AnnotationAlgorithm, String> annotations) {
    public static JSONAnnotation from(final VariantAnnotations annotation) {
        final var results = annotation.annotations().stream().collect(Collectors.toMap(Annotation::algorithm, Annotation::result));
        return new JSONAnnotation(JSONVariant.from(annotation.variant()), results);
    }

    private record JSONVariant(String chromosome,
                               long position,
                               String referenceAllele,
                               String alternativeAllele,
                               String type,
                               String gene) {

        public static JSONVariant from(final Variant variant) {
            return new JSONVariant(variant.getChromosome(), variant.getPosition(), variant.getReferenceAllele().toString(),
                    variant.getAlternativeAllele().toString(), variant.getType().name(), variant.getGene());
        }
    }
}
