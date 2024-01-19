package com.annotator.core.domain.annotation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnotationService {
    private final VariantsAnnotationsRepository annotationsRepository;

    public List<VariantAnnotations> createMissingAnnotations(final List<Variant> variants, final List<AnnotationAlgorithm> algorithms) {
        return annotationsRepository.findWithMissingAlgorithms(variants, algorithms);
    }

    private VariantAnnotations createMissingAnnotation(final Variant variant) {
        final var newVariant = VariantAnnotations.from(variant);
        annotationsRepository.save(newVariant);
        return newVariant;
    }
}
