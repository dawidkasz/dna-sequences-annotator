package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.Annotation;
import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.AnnotationService;
import com.annotator.core.domain.annotation.VariantAnnotations;
import com.annotator.core.domain.order.request.AnnotationRequest;
import com.annotator.core.domain.order.request.AnnotationRequestPublisher;
import com.annotator.core.domain.order.result.AnnotationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final AnnotationRequestPublisher requestPublisher;
    private final AnnotationService annotationService;
    private final List<OrderNotifier> orderNotifiers;

    //Create requests for missing  algorithms
    private static Stream<AnnotationRequest> createRequests(final OrderId orderId, final VariantAnnotations newAnnotation,
                                                            final List<AnnotationAlgorithm> algorithms) {
        final var id = newAnnotation.annotationId();
        final var variant = newAnnotation.variant();
        final var presentAlgorithms = newAnnotation.annotations().stream().map(Annotation::algorithm).collect(Collectors.toSet());
        return algorithms.stream()
                .filter(algorithm -> !presentAlgorithms.contains(algorithm))
                .map(algorithm -> AnnotationRequest.from(orderId, id, variant, algorithm));
    }

    public void handle(final Order order) {
        final var orderId = order.getOrderId();
        final var requests = annotationService.createMissingAnnotations(order.getVariants(), order.getAlgorithms()).stream()
                .flatMap(variantAnnotations -> createRequests(orderId, variantAnnotations, order.getAlgorithms()))
                .toList();

        final var orderRequestsIds = requests.stream()
                .map(AnnotationRequest::annotationRequestId);

        order.setRequestIds(orderRequestsIds.collect(Collectors.toSet()));
        orderRepository.save(order);

        if (order.isFinished()) {
            handleFinishedOrder(order);
            return;
        }

        requests.forEach(requestPublisher::publish);
    }

    public void updateOrderWithResult(final AnnotationResult result) {
        orderRepository.find(result.orderId()).ifPresent(order -> {
                    order.acceptResult(result.annotationRequestId());
                    orderRepository.save(order);

                    if (order.isFinished()) {
                        handleFinishedOrder(order);
                    }
                }
        );
    }

    public void handleFinishedOrder(final Order order) {
        if (!order.isFinished()) {
            throw new IllegalArgumentException("Provided order is not finished");
        }
        orderNotifiers.forEach(notifier -> notifier.notifyAbutOrderFinished(order));
    }

}
