package com.annotator.core.domain;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AnnotationId {
    private final UUID uuid;

    public static AnnotationId create() {
        return new AnnotationId(UUID.randomUUID());
    }

    public static AnnotationId from(String id) {
        return new AnnotationId(UUID.fromString(id));
    }

    public String getId() {
        return uuid.toString();
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
