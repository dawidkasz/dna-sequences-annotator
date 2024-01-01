package com.annotator.core;

import com.annotator.core.domain.annotation.VariantsAnnotationsRepository;
import com.annotator.core.domain.order.OrderRepository;
import com.annotator.core.helper.StubsFactory;
import com.annotator.core.infrastructure.web.annotations.JSONAnnotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebTest {
    @Value("${local.server.port}")
    protected int PORT;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    VariantsAnnotationsRepository annotationResultRepository;
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void shouldRetrieveOrderById() {
        //given
        StubsFactory.variantAnnotations().forEach(annotationResultRepository::save);
        final var order = StubsFactory.finishedOrder();
        orderRepository.save(order);

        final var response = annotationResults(order.getOrderId().uuid());
        Assertions.assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    protected ResponseEntity<JSONAnnotation[]> annotationResults(final UUID orderId) {
        final var uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .port(PORT)
                .host("localhost")
                .path("/annotate")
                .path("/results")
                .path("/{id}")
                .build(orderId.toString());
        return restTemplate.getForEntity(uri, JSONAnnotation[].class);
    }
}
