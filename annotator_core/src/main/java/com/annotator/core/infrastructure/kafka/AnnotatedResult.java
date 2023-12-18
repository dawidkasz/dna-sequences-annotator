package com.annotator.core.infrastructure.kafka;

import lombok.Data;

@Data
public class AnnotatedResult {


    private Id annotationId;
    private String result;

    @Data
    static
    class Id {
        private String id;
    }
}
