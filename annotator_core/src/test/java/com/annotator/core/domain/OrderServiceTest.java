package com.annotator.core.domain;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.AnnotationService;
import com.annotator.core.domain.order.Order;
import com.annotator.core.domain.order.OrderNotifier;
import com.annotator.core.domain.order.OrderRepository;
import com.annotator.core.domain.order.OrderService;
import com.annotator.core.domain.order.request.AnnotationRequestId;
import com.annotator.core.domain.order.request.AnnotationRequestPublisher;
import com.annotator.core.helper.StubsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final AnnotationRequestPublisher requestPublisher = mock(AnnotationRequestPublisher.class);
    private final AnnotationService annotationService = mock(AnnotationService.class);
    private final List<OrderNotifier> orderNotifiers = List.of(mock(OrderNotifier.class));
    private OrderService service;
    private Order order;

    @BeforeEach
    public void init() {
        service = new OrderService(orderRepository, requestPublisher, annotationService, orderNotifiers);
        order = StubsFactory.finishedOrder();
    }

    @Test
    public void shouldSaveOrderAndNotify_whenOrderAlreadyFinished() {
        //given
        when(orderRepository.notExists(any())).thenReturn(false);

        //when
        service.handle(order);

        //then
        assertTrue(order.isFinished());
        assertTrue(order.getRequestIds().isEmpty());
        assertEquals(StubsFactory.variants().size(), order.getPositions().count());


        verifyNoInteractions(requestPublisher);
        verify(orderRepository, times(1)).save(order);
        verify(orderNotifiers.get(0), times(1)).notifyAbutOrderFinished(order);
    }

    @Test
    public void shouldSaveOrderAndPublishRequests_whenOrderNotFinished() {
        //given
        final var annotations = StubsFactory.variantAnnotations().subList(0, 2);
        when(annotationService.createMissingAnnotations(any())).thenReturn(annotations);

        //when
        service.handle(order);

        //then
        assertFalse(order.isFinished());
        assertEquals(StubsFactory.variants().size(), order.getPositions().count());
        assertEquals(2L, order.getRequestIds().size());


        verify(requestPublisher, times(annotations.size() * AnnotationAlgorithm.values().length)).publish(any());
        verify(orderRepository, times(1)).save(order);
        verifyNoInteractions(orderNotifiers.get(0));
    }

    @Test
    public void shouldUpdateOrderAndNotify_whenResultPassedAndOrderFinished() {
        //given
        final var result = StubsFactory.annotationResult(order.getOrderId());
        order.setRequestIds(new HashSet<>(List.of(result.annotationRequestId())));
        when(orderRepository.find(order.getOrderId())).thenReturn(Optional.ofNullable(order));

        //when
        service.updateOrderWithResult(result);

        //then
        assertTrue(order.isFinished());
        assertTrue(order.getRequestIds().isEmpty());

        verify(orderRepository, times(1)).save(order);
        verify(orderNotifiers.get(0), times(1)).notifyAbutOrderFinished(order);
    }


    @Test
    public void shouldUpdateOrderButNotNotify_whenResultPassedAndOrderNotFinished() {
        //given
        final var result = StubsFactory.annotationResult(order.getOrderId());
        order.setRequestIds(new HashSet<>(List.of(result.annotationRequestId(), AnnotationRequestId.create())));
        when(orderRepository.find(order.getOrderId())).thenReturn(Optional.ofNullable(order));

        //when
        service.updateOrderWithResult(result);

        //then
        assertFalse(order.isFinished());
        assertEquals(1L, order.getRequestIds().size());

        verify(orderRepository, times(1)).save(order);
        verifyNoInteractions(orderNotifiers.get(0));
    }
}