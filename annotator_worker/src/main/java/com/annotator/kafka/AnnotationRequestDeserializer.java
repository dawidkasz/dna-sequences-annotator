package com.annotator.kafka;

import com.annotator.domain.AnnotationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class AnnotationRequestDeserializer implements Deserializer<AnnotationRequest> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AnnotationRequest deserialize(final String topic, final byte[] data) {
        try {
            if (data == null) {
                log.error("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), AnnotationRequest.class);
        } catch (final Exception e) {
            throw new SerializationException("Error when deserializing byte[] to AnnotationRequest " + e.getMessage());
        }
    }
}
