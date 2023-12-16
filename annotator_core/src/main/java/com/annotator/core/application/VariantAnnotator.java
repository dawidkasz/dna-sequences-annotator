package com.annotator.core.application;

import com.annotator.core.application.variantparser.VariantParser;
import com.annotator.core.domain.AnnotationAlgorithm;
import com.annotator.core.domain.AnnotationRequest;
import com.annotator.core.domain.AnnotationId;
import com.annotator.core.domain.Variant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantAnnotator {
    private final VariantParser parser;
    private final AnnotationProducer annotationProducer;
    private final VariantRepository variantRepository;

    public AnnotationId annotate(InputStream variantFileStream, List<AnnotationAlgorithm> algorithms) {
        List<Variant> variants = parser.read(variantFileStream);

        var batchId = AnnotationId.create();
        variants.forEach(variant -> annotateSingleVariant(batchId, variant, algorithms));

        return batchId;
    }

    private void annotateSingleVariant(AnnotationId annotationId, Variant variant, List<AnnotationAlgorithm> algorithms) {
        boolean exists = variantRepository.find(
                variant.getChromosome(),
                variant.getPosition(),
                variant.getReferenceAllele(),
                variant.getAlternativeAllele()
        ).isPresent();

        if (!exists) { // just to demonstrate usage, only temporary solution
            variantRepository.save(variant);
        }

        algorithms.forEach(alg ->  annotationProducer.annotate(new AnnotationRequest(annotationId, variant, alg)));
    }
}
