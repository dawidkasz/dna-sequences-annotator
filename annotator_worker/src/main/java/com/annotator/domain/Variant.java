package com.annotator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Variant {
    private String chromosome;
    private long position;
    private String referenceAllele;
    private String alternativeAllele;
    private String gene;
}
