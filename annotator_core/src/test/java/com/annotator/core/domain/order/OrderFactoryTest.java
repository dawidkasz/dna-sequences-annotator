package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.Variant;
import com.annotator.core.helper.StubsFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderFactoryTest {

    List<Variant> variants = StubsFactory.variants();

    @Test
    void shouldCreateOrder() {
        //given
        final var orderFactory = new OrderFactory();
        final var algorithm = AnnotationAlgorithm.PANGOLIN;
        final var exceptedPositions = variants.stream().map(variant -> new OrderPosition(variant, algorithm)).toList();

        //when
        final var order = orderFactory.createOrder(variants, List.of(algorithm));
        final var positions = order.getPositions().toList();

        //then
        Assertions.assertNotNull(order.getOrderId());
        assertThat(positions).containsAll(exceptedPositions);
    }

}