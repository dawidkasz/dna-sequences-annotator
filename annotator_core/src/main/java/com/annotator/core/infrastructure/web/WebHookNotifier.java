package com.annotator.core.infrastructure.web;

import com.annotator.core.domain.order.Order;
import com.annotator.core.domain.order.OrderNotifier;
import com.annotator.core.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.notify.webhook", name = "enabled", havingValue = "true")
public class WebHookNotifier implements OrderNotifier {
    private final AnnotationResultsClient resultsClient;
    private final OrderRepository orderRepository;
    @Value("${spring.notify.webhook.url}")
    private String webhookUrl;

    @Override
    public void notifyAbutOrderFinished(final Order order) {
        final var results = orderRepository.findOrderAnnotations(order.getOrderId());
        resultsClient.sendResults(results, URI.create(webhookUrl));
    }
}
