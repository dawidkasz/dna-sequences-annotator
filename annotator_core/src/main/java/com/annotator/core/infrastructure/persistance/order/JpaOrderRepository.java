package com.annotator.core.infrastructure.persistance.order;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.order.*;
import com.annotator.core.domain.order.request.AnnotationRequestId;
import com.annotator.core.infrastructure.persistance.annotation.JpaAnnotationRepository;
import com.annotator.core.infrastructure.persistance.annotation.JpaVariant;
import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepository implements OrderRepository {
    private final SpringDataOrderRepository orderRepository;
    private final SpringDataVariantRepository variantRepository;
    private final JpaAnnotationRepository annotationRepository;

    private static JpaOrder toJpa(final Order order, final List<Long> variantIds) {
        return JpaOrder.builder()
                .orderId(order.getOrderId().uuid())
                .variants(variantIds)
                .algorithms(order.getAlgorithms().stream().map(AnnotationAlgorithm::name).toList())
                .requestsIds(order.getRequestIds().stream().map(AnnotationRequestId::getUuid).toList())
                .build();
    }

    private static Order toDomain(final JpaOrder order, final List<JpaOrderVariant> jpaVariants) {
        final var variants = jpaVariants.stream().map(JpaOrderVariant::getVariant).map(JpaVariant::toVariant).toList();
        final var algorithms = order.getAlgorithms().stream().map(AnnotationAlgorithm::valueOf).toList();
        return Order.builder()
                .orderId(new OrderId(order.getOrderId()))
                .orderPositions(new OrderPositions(variants, algorithms))
                .requestIds(order.getRequestsIds().stream().map(AnnotationRequestId::from).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public boolean notExists(final OrderPosition orderPosition) {
        return annotationRepository.exists(orderPosition.variant());
    }

    @Override
    @Transactional
    public void save(final Order order) {
        final var variantsIds = order.getVariants().stream()
                .map(JpaVariant::from)
                .map(variant -> variantRepository.findByVariant(variant).orElseGet(() -> variantRepository.save(new JpaOrderVariant(variant))))
                .map(JpaOrderVariant::getId)
                .toList();

        orderRepository.save(toJpa(order, variantsIds));
    }


    @Override
    @Transactional
    public Optional<Order> find(final OrderId orderId) {
        return orderRepository.findByOrderId(orderId.uuid())
                .map(jpaOrder -> {
                    final var ids = jpaOrder.getVariants();
                    final var variants = Lists.newArrayList(variantRepository.findAllById(ids));
                    return toDomain(jpaOrder, variants);
                });
    }
}
