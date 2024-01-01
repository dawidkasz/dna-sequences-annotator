package com.annotator.core;

import com.annotator.core.domain.annotation.VariantsAnnotationsRepository;
import com.annotator.core.domain.order.OrderRepository;
import com.annotator.core.domain.order.request.AnnotationRequestId;
import com.annotator.core.helper.StubsFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    VariantsAnnotationsRepository annotationResultRepository;


    @Test
    public void shouldSaveOrder() {
        final var order = StubsFactory.finishedOrder();
        orderRepository.save(order);
        Assertions.assertTrue(true);
    }

    @Test
    public void shouldSaveAnnotation() {
        final var annotation = StubsFactory.variantAnnotations();
        annotation.forEach(annotationResultRepository::save);
        Assertions.assertTrue(true);
    }

    @Test
    public void shouldRetrieveAnnotationById() {
        //given
        final var annotation = StubsFactory.variantAnnotations().get(0);
        annotationResultRepository.save(annotation);

        //when
        final var response = annotationResultRepository.findById(annotation.annotationId()).orElseThrow();

        //then
        assertEquals(annotation.annotationId(), response.annotationId());
        assertThat(response.annotations()).hasSameElementsAs(annotation.annotations());
    }

    @Test
    public void shouldRetrieveAnnotationByVariant() {
        //given
        final var annotation = StubsFactory.variantAnnotations().get(0);
        annotationResultRepository.save(annotation);

        //when
        final var response = annotationResultRepository.findByVariant(annotation.variant()).orElseThrow();

        //then
        assertEquals(annotation.annotationId(), response.annotationId());
        assertThat(response.annotations()).hasSameElementsAs(annotation.annotations());
    }

    @Test
    public void shouldRetrieveOrderById() {
        //given
        final var order = StubsFactory.finishedOrder();
        order.setRequestIds(Set.of(AnnotationRequestId.create()));
        orderRepository.save(order);

        final var response = orderRepository.find(order.getOrderId()).orElseThrow();
        assertThat(response).usingRecursiveComparison().isEqualTo(order);
    }
}
