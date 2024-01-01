package com.annotator.core.domain.order;

import java.util.UUID;

public record OrderId(UUID uuid) {
    public static OrderId nextIdentity() {
        return new OrderId(UUID.randomUUID());
    }

    public String getId() {
        return uuid.toString();
    }

}
