package com.annotator.core.domain.order.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AnnotationRequestId {
    private final UUID uuid;

    public static AnnotationRequestId create() {
        return new AnnotationRequestId(UUID.randomUUID());
    }

    public static AnnotationRequestId from(final String id) {
        return new AnnotationRequestId(UUID.fromString(id));
    }

    public static AnnotationRequestId from(final UUID uuid) {
        return new AnnotationRequestId(uuid);
    }

    public String getId() {
        return uuid.toString();
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
