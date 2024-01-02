package com.annotator.core.domain.order;

import java.util.Optional;

public interface OrderRepository {
    boolean notExists(OrderPosition orderPosition);

    void save(Order order);

    Optional<Order> find(OrderId orderId);

}
