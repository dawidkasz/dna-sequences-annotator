package com.annotator.core.infrastructure.kafka;

import lombok.Data;

@Data
public class AnnotatedResult {


    private Id annotationId;
    private String chromosome;
    private long position;
    private String referenceAllele;
    private String alternativeAllele;
    private String algorithm;
    private String result;

    @Data
    static class Id {
        private String id;
    }

}
