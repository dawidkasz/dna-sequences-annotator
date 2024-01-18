package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.VariantAnnotations;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    boolean notExists(OrderPosition orderPosition);

    void save(Order order);

    Optional<Order> find(OrderId orderId);

    List<AnnotatedOrder> findAll();

    List<VariantAnnotations> findOrderAnnotations(OrderId orderId);

}
