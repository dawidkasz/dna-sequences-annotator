package com.annotator.core.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class AnnotatedResultDeserializer implements Deserializer<AnnotatedResult> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AnnotatedResult deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.error("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(data, AnnotatedResult.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to AnnotationRequest " + e.getMessage());
        }
    }
}
