package com.annotator.core.helper;

import com.annotator.core.domain.annotation.*;
import com.annotator.core.domain.order.Order;
import com.annotator.core.domain.order.OrderFactory;
import com.annotator.core.domain.order.OrderId;
import com.annotator.core.domain.order.request.AnnotationRequest;
import com.annotator.core.domain.order.result.AnnotationResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StubsFactory {

    public static List<Variant> variants() {
        return List.of(
                Variant.builder().referenceAllele("A").alternativeAllele("T").chromosome("CHROM1").position(10L).gene("gene1").build(),
                Variant.builder().referenceAllele("C").alternativeAllele("T").chromosome("CHROM2").position(12L).gene("gene2").build(),
                Variant.builder().referenceAllele("G").alternativeAllele("A").chromosome("CHROM3").position(13L).gene("gene3").build()
        );
    }

    public static Order finishedOrder() {
        return new OrderFactory().createOrder(variants(), List.of(AnnotationAlgorithm.PANGOLIN));
    }

    public static List<VariantAnnotations> variantAnnotations() {
        final var annotation1 = List.of(new Annotation(AnnotationAlgorithm.PANGOLIN, "test"), new Annotation(AnnotationAlgorithm.SPIP, "test2"));
        final var annotation2 = List.of(new Annotation(AnnotationAlgorithm.PANGOLIN, "test3"), new Annotation(AnnotationAlgorithm.SPIP, "test4"));
        return List.of(VariantAnnotations.from(variants().get(0), annotation1), VariantAnnotations.from(variants().get(1), annotation2));
    }

    public static List<AnnotationRequest> requests(final OrderId orderId) {
        return variants().stream().map(variant -> AnnotationRequest.from(orderId, AnnotationId.nextIdentity(), variants().get(0), AnnotationAlgorithm.PANGOLIN)).toList();
    }

    public static AnnotationResult annotationResult(final OrderId orderId, final AnnotationRequest request) {
        return new AnnotationResult(request.annotationRequestId(), orderId, request.annotationId(), AnnotationAlgorithm.PANGOLIN, "test");
    }

    public static AnnotationResult annotationResult(final OrderId orderId) {
        return annotationResult(orderId, requests(orderId).get(0));
    }

    public static List<AnnotationResult> annotationResults(final OrderId orderId) {
        return requests(orderId).stream().map(request -> annotationResult(orderId, request)).toList();
    }

    public static List<Annotation> annotations() {
        return List.of(
                new Annotation(AnnotationAlgorithm.SPIP, "test"),
                new Annotation(AnnotationAlgorithm.PANGOLIN, "test")
        );
    }


}
