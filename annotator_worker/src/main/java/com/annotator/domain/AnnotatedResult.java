package com.annotator.domain;

public record AnnotatedResult(String algorithmName, Variant variant, String result) {
    public static AnnotatedResult from(Annotation annotation, String result) {
        return new AnnotatedResult(annotation.algorithmName(), annotation.variant(), result);
    }
}
