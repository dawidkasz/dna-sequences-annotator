package com.annotator.kafka;

import com.annotator.domain.AnnotatedResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class AnnotatedResultSerializer implements Serializer<AnnotatedResult> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(final String topic, final AnnotatedResult data) {
        try {
            if (data == null) {
                log.error("Null received at serializing");
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (final Exception e) {
            throw new SerializationException("Error when serializing AnnotatedResult to byte[]");
        }
    }
}
