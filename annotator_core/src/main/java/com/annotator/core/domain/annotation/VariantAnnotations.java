package com.annotator.core.domain.annotation;

import java.util.ArrayList;
import java.util.List;

public record VariantAnnotations(AnnotationId annotationId, Variant variant, List<Annotation> annotations) {
    public static VariantAnnotations from(final Variant variant, final List<Annotation> annotations) {
        return new VariantAnnotations(AnnotationId.nextIdentity(), variant, annotations);
    }

    public static VariantAnnotations from(final Variant variant) {
        return VariantAnnotations.from(variant, new ArrayList<>());
    }
}
