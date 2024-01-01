package com.annotator.core.infrastructure.persistance.order;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaOrder {

    @Id
    private UUID orderId;

    @Type(ListArrayType.class)
    @Column(
            name = "algorithms",
            columnDefinition = "text[]"
    )
    private List<String> algorithms;
    @Type(ListArrayType.class)
    @Column(
            name = "requests_ids",
            columnDefinition = "uuid[]"
    )
    private List<UUID> requestsIds;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "bigint[]")
    private List<Long> variants = new ArrayList<>();
}
