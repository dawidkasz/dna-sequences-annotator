package com.annotator.core.application;

import com.annotator.core.application.variantparser.VariantParser;
import com.annotator.core.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantAnnotator implements AnnotationConsumer {
    private final VariantParser parser;
    private final AnnotationProducer annotationProducer;
    private final VariantRepository variantRepository;
    private final AnnotationRepository annotationRepository;

    public AnnotationId annotate(final InputStream variantFileStream, final List<AnnotationAlgorithm> algorithms) {
        final List<Variant> variants = parser.read(variantFileStream);

        final var batchId = AnnotationId.create();
        variants.forEach(variant -> annotateSingleVariant(batchId, variant, algorithms));

        return batchId;
    }

    private void annotateSingleVariant(final AnnotationId annotationId, final Variant variant, final List<AnnotationAlgorithm> algorithms) {
        final boolean exists = variantRepository.find(
                variant.getChromosome(),
                variant.getPosition(),
                variant.getReferenceAllele(),
                variant.getAlternativeAllele()
        ).isPresent();

        if (!exists) { // just to demonstrate usage, only temporary solution
            variantRepository.save(variant);
        }

        algorithms.forEach(alg -> annotationProducer.annotate(new AnnotationRequest(annotationId, variant, alg)));
    }

    @Override
    public void consumeAnnotation(final Annotation annotation) {
        log.info("Processed {}", annotation);
        annotationRepository.save(annotation);
    }
}
