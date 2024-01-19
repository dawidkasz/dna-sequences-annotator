package com.annotator.core.infrastructure.persistance.order;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.VariantAnnotations;
import com.annotator.core.domain.order.*;
import com.annotator.core.domain.order.request.AnnotationRequestId;
import com.annotator.core.infrastructure.persistance.annotation.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepository implements OrderRepository {
    private final SpringDataOrderRepository orderRepository;
    private final JpaAnnotationRepository annotationRepository;
    private final JpaVariantRepository variantRepository;

    private static JpaOrder toJpa(final Order order, final List<Long> variantIds) {
        return JpaOrder.builder()
                .orderId(order.getOrderId().uuid())
                .variants(variantIds)
                .algorithms(order.getAlgorithms().stream().map(AnnotationAlgorithm::name).toList())
                .requestsIds(order.getRequestIds().stream().map(AnnotationRequestId::getUuid).toList())
                .build();
    }

    private static Order toDomain(final JpaOrder order, final List<JpaVariant> jpaVariants) {
        final var variants = jpaVariants.stream().map(JpaVariant::getVariant).map(JpaVariantDetails::toVariant).toList();
        final var algorithms = order.getAlgorithms().stream().map(AnnotationAlgorithm::valueOf).toList();
        return Order.builder()
                .orderId(new OrderId(order.getOrderId()))
                .orderPositions(new OrderPositions(variants, algorithms))
                .requestIds(order.getRequestsIds().stream().map(AnnotationRequestId::from).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public boolean notExists(final OrderPosition orderPosition) {
        return !annotationRepository.exists(orderPosition.variant());
    }

    @Override
    @Transactional
    public void save(final Order order) {
        final var jpaVariants = order.getVariants().stream().map(JpaVariantDetails::from).toList();
        final var variantsIds = variantRepository.findAllIds(jpaVariants);
        orderRepository.save(toJpa(order, variantsIds));
    }


    @Override
    @Transactional
    public Optional<Order> find(final OrderId orderId) {
        return orderRepository.findByOrderId(orderId.uuid())
                .map(jpaOrder -> {
                    final var ids = jpaOrder.getVariants();
                    final var variants = variantRepository.findAllByVariants(ids);

                    if (variants.size() != ids.size()) {
                        throw new IllegalStateException("Cannot retrieve order - not all variants are present in database");
                    }
                    return toDomain(jpaOrder, variants);
                });
    }

    @Override
    public List<AnnotatedOrder> findAll() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(order -> new AnnotatedOrder(
                        new OrderId(order.getOrderId()),
                        findOrderAnnotations(new OrderId(order.getOrderId()))
                ))
                .toList();
    }

    @Override
    public List<VariantAnnotations> findOrderAnnotations(final OrderId orderId) {
        return orderRepository.findByOrderId(orderId.uuid())
                .map(JpaOrder::getVariants)
                .map(annotationRepository::findAllByVariantsId)
                .orElseGet(Collections::emptyList).stream()
                .map(JpaAnnotation::toAnnotation)
                .toList();
    }
}
