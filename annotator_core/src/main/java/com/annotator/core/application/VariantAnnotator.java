package com.annotator.core.application;

import com.annotator.core.application.variantparser.VariantParser;
import com.annotator.core.domain.annotation.*;
import com.annotator.core.domain.order.OrderFactory;
import com.annotator.core.domain.order.OrderId;
import com.annotator.core.domain.order.OrderRepository;
import com.annotator.core.domain.order.OrderService;
import com.annotator.core.domain.order.result.AnnotationResult;
import com.annotator.core.domain.order.result.AnnotationResultConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantAnnotator implements AnnotationResultConsumer {
    private final VariantParser parser;
    private final OrderService orderService;
    private final OrderFactory orderFactory;
    private final OrderRepository orderRepository;
    private final AnnotationService annotationService;
    private final VariantsAnnotationsRepository annotationsRepository;

    public OrderId annotate(final InputStream variantFileStream, final List<AnnotationAlgorithm> algorithms) {
        final List<Variant> variants = parser.read(variantFileStream);

        final var order = orderFactory.createOrder(variants, algorithms);

        orderService.handle(order);

        return order.getOrderId();
    }

    public List<VariantAnnotations> retrieveAnnotations(final OrderId orderId) {
        return orderRepository.findOrderAnnotations(orderId);
    }

    @Override
    public void consume(final AnnotationResult result) {
        log.info("Processed {}", result);
        annotationsRepository.save(result);
        orderService.updateOrderWithResult(result);
    }
}
