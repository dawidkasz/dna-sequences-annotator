package com.annotator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Data
public class AnnotationId {
    @JsonProperty("id")
    private UUID uuid;
}
