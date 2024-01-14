package com.annotator.core.domain.annotation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnotationService {
    private final VariantsAnnotationsRepository annotationsRepository;

    public List<VariantAnnotations> createMissingAnnotations(final List<Variant> variants) {
        return variants.stream()
                .filter(variant -> !annotationsRepository.isAnnotated(variant))
                .map(this::createMissingAnnotation)
                .toList();
    }

    private VariantAnnotations createMissingAnnotation(final Variant variant) {
        final var newVariant = VariantAnnotations.from(variant);
        annotationsRepository.save(newVariant);
        return newVariant;
    }
}
