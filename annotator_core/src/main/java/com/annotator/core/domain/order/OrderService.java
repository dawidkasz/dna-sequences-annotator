package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.AnnotationService;
import com.annotator.core.domain.annotation.VariantAnnotations;
import com.annotator.core.domain.order.request.AnnotationRequest;
import com.annotator.core.domain.order.request.AnnotationRequestPublisher;
import com.annotator.core.domain.order.result.AnnotationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AnnotationRequestPublisher requestPublisher;
    private final AnnotationService annotationService;
    private final List<OrderNotifier> orderNotifiers;

    //Create requests for all possible algorithms
    private static Stream<AnnotationRequest> createRequests(final OrderId orderId, final VariantAnnotations newAnnotation) {
        final var id = newAnnotation.annotationId();
        final var variant = newAnnotation.variant();
        return Arrays.stream(AnnotationAlgorithm.values())
                .map(algorithm -> AnnotationRequest.from(orderId, id, variant, algorithm));
    }

    public void handle(final Order order) {
        final var orderId = order.getOrderId();
        final var requests = annotationService.createMissingAnnotations(order.getVariants()).stream()
                .flatMap(variantAnnotations -> createRequests(orderId, variantAnnotations))
                .toList();

        final var orderRequestsIds = requests.stream()
                .filter(request -> order.getAlgorithms().contains(request.algorithm()))
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
