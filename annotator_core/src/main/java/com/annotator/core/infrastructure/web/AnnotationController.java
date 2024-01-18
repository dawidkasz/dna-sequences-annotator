package com.annotator.core.infrastructure.web;

import com.annotator.core.application.VariantAnnotator;
import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.order.Order;
import com.annotator.core.domain.order.OrderId;
import com.annotator.core.domain.order.OrderRepository;
import com.annotator.core.infrastructure.web.annotations.JSONAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/annotate")
public class AnnotationController {
    private final VariantAnnotator variantProcessor;
    private final OrderRepository orderRepository;


    @PostMapping("/csv")
    public ResponseEntity<OrderResponse> annotateCsvFile(
            @RequestParam("file") final MultipartFile file,
            @RequestParam(value = "algorithms", defaultValue = "PANGOLIN") final String algorithmParam
    ) {
        final List<AnnotationAlgorithm> algorithms = Collections.singletonList(AnnotationAlgorithm.valueOf(algorithmParam));

        log.info("Annotation variants using {} algorithms", algorithms);

        try {
            final var orderId = variantProcessor.annotate(file.getInputStream(), algorithms);
            return ResponseEntity.ok(new OrderResponse(orderId.getId()));
        } catch (final IOException e) {
            log.debug("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/results/{orderId}")
    public ResponseEntity<OrderResultResponse> annotationsResults(@PathVariable final UUID orderId) {
        final var id = new OrderId(orderId);
        if (orderRepository.find(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final var annotations = orderRepository.findOrderAnnotations(id);
        final var result = new OrderResultResponse(id.getId(), annotations.stream().map(JSONAnnotation::from).toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/results/list")
    public List<OrderResultResponse> listAnnotationResults() {
        System.out.println("aaa");
        return orderRepository.findAll().stream()
                .map(o -> new OrderResultResponse(
                        o.orderId().getId(),
                        o.annotations().stream().map(JSONAnnotation::from).toList()
                ))
                .sorted()
                .toList();
    }

    public record OrderResponse(String annotationId) {
    }

    public record OrderResultResponse(String orderId, List<JSONAnnotation> annotations){
    }

}
