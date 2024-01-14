package com.annotator.core.infrastructure.console;

import com.annotator.core.domain.order.Order;
import com.annotator.core.domain.order.OrderNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsoleNotifier implements OrderNotifier {
    @Override
    public void notifyAbutOrderFinished(final Order order) {
        log.info("Order: {} has finished!", order.getOrderId());
    }
}
