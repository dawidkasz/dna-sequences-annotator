package com.annotator.core.infrastructure.persistance.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataOrderRepository extends CrudRepository<JpaOrder, Long> {
    Optional<JpaOrder> findByOrderId(UUID orderId);
}
