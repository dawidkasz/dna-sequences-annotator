package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.Variant;

import java.util.List;
import java.util.stream.Stream;

public record OrderPositions(List<Variant> variants, List<AnnotationAlgorithm> algorithms) {
    Stream<OrderPosition> getPositions() {
        return variants.stream()
                .flatMap(orderVariant -> algorithms.stream()
                        .map(alg -> new OrderPosition(orderVariant, alg)));
    }

    public long size() {
        return (long) variants.size() * algorithms.size();
    }
}
