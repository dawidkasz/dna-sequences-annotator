package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.VariantAnnotations;

import java.util.List;

public record AnnotatedOrder(OrderId orderId, List<VariantAnnotations> annotations) {
}
