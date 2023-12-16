package com.annotator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class CalculationDeserializer implements Deserializer<Calculation> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Calculation deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.error("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), Calculation.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to CalculationResult");
        }
    }
}