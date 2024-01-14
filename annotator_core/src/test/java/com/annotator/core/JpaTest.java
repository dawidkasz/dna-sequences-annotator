package com.annotator.core;

import com.annotator.core.domain.annotation.Annotation;
import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.VariantAnnotations;
import com.annotator.core.domain.annotation.VariantsAnnotationsRepository;
import com.annotator.core.domain.order.OrderId;
import com.annotator.core.domain.order.OrderRepository;
import com.annotator.core.domain.order.request.AnnotationRequestId;
import com.annotator.core.domain.order.result.AnnotationResult;
import com.annotator.core.helper.StubsFactory;
import com.annotator.core.infrastructure.persistance.annotation.JpaVariant;
import com.annotator.core.infrastructure.persistance.annotation.JpaVariantDetails;
import com.annotator.core.infrastructure.persistance.annotation.SpringDataAnnotationRepository;
import com.annotator.core.infrastructure.persistance.annotation.SpringDataVariantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    VariantsAnnotationsRepository annotationResultRepository;
    @Autowired
    SpringDataVariantRepository variantRepository;
    @Autowired
    SpringDataAnnotationRepository annotationRepository;


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
        StubsFactory.variants().stream().map(JpaVariantDetails::from).map(JpaVariant::new).forEach(variantRepository::save);
        annotationResultRepository.save(annotation);

        //when
        final var response = annotationResultRepository.findById(annotation.annotationId()).orElseThrow();

        //then
        assertEquals(annotation.annotationId(), response.annotationId());
        assertThat(response.annotations()).hasSameElementsAs(annotation.annotations());
    }

    @Test
    public void shouldSaveTwoResults() {
        //given
        final var annotation = StubsFactory.variantAnnotations().get(0);
        StubsFactory.variants().stream().map(JpaVariantDetails::from).map(JpaVariant::new).forEach(variantRepository::save);
        final var anno1 = VariantAnnotations.from(StubsFactory.variants().get(0), List.of(new Annotation(AnnotationAlgorithm.SPIP, "test")));

        annotationResultRepository.save(anno1);
        final var anno2 = VariantAnnotations.from(StubsFactory.variants().get(0), List.of(new Annotation(AnnotationAlgorithm.PANGOLIN, "test")));
        annotationResultRepository.save(anno2);

        //when
        final var response = annotationResultRepository.findById(annotation.annotationId()).orElseThrow();

        //then
        assertEquals(annotation.annotationId(), response.annotationId());
        assertThat(response.annotations()).hasSameElementsAs(annotation.annotations());
    }

    @Test
    public void shouldSaveTwoAnnotationResults() {
        //given
        final var annotation = StubsFactory.variantAnnotations().get(0);
        StubsFactory.variants().stream().map(JpaVariantDetails::from).map(JpaVariant::new).forEach(variantRepository::save);
        final var anno1 = VariantAnnotations.from(StubsFactory.variants().get(0), List.of());
        annotationResultRepository.save(anno1);
        final var res = annotationResultRepository.findByVariant(StubsFactory.variants().get(0)).orElseThrow();
        final var order = OrderId.nextIdentity();
        annotationResultRepository.save(new AnnotationResult(AnnotationRequestId.create(), order, res.annotationId(), AnnotationAlgorithm.SPIP, "test"));
        annotationResultRepository.save(new AnnotationResult(AnnotationRequestId.create(), order, res.annotationId(), AnnotationAlgorithm.PANGOLIN, "test"));

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
