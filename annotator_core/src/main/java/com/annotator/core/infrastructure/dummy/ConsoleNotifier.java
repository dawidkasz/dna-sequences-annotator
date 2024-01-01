package com.annotator.core.infrastructure.dummy;

import com.annotator.core.domain.order.Order;
import com.annotator.core.domain.order.OrderNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsoleNotifier implements OrderNotifier {
    @Override
    public void notifyAbutOrderFinished(final Order order) {
        log.warn("Order: {} has finished!", order.getOrderId());
    }
}
