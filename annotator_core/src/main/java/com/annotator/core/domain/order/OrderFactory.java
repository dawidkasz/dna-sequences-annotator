package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.Variant;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFactory {

    public Order createOrder(final List<Variant> variants, final List<AnnotationAlgorithm> algorithms) {
        return Order.builder()
                .orderId(OrderId.nextIdentity())
                .orderPositions(new OrderPositions(variants, algorithms))
                .build();
    }
}
