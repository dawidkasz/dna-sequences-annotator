package com.annotator.domain;

public record AnnotatedResult(AnnotationId annotationId,
                              String chromosome,
                              long position,
                              String referenceAllele,
                              String alternativeAllele,
                              String algorithm,
                              String result) {
}
