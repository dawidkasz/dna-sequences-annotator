package com.annotator.core.infrastructure.web;

import com.annotator.core.application.VariantAnnotator;
import com.annotator.core.domain.AnnotationAlgorithm;
import com.annotator.core.domain.AnnotationId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/annotate")
public class AnnotationController {
    private final VariantAnnotator variantProcessor;

    @PostMapping("/csv")
    public ResponseEntity<AnnotationResponse> annotateVcfFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "algorithms", defaultValue = "PANGOLIN") String algorithmParam
    ) {
        List<AnnotationAlgorithm> algorithms = Collections.singletonList(AnnotationAlgorithm.valueOf(algorithmParam));

        log.info("Annotation variants using {} algorithms", algorithms);

        try {
            final AnnotationId annotationId = variantProcessor.annotate(file.getInputStream(), algorithms);
            return ResponseEntity.ok(new AnnotationResponse(annotationId.getId()));
        } catch (final IOException e) {
            log.debug("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    private record AnnotationResponse(String annotationId) {
    }
}
