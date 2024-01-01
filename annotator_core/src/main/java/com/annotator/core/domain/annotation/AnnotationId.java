package com.annotator.core.domain.annotation;

import java.util.UUID;

public record AnnotationId(UUID uuid) {
    public static AnnotationId nextIdentity() {
        return new AnnotationId(UUID.randomUUID());
    }

    public String getId() {
        return uuid.toString();
    }
}
