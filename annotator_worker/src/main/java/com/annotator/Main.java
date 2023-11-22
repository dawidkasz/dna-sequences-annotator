package com.annotator;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Main {
    static Properties createConsumerProps() {
        String bootstrapServers = "127.0.0.1:29092";
        String groupId = "my-fourth-application";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CalculationDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, "test-1");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    static Properties createProducerProps() {
        String bootstrapServers = "127.0.0.1:29092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CalculationResultSerializer.class.getName());
        properties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "producer-1");
        return properties;
    }

    public static void main(String[] args) {
        String topic = "demo_pzsp2";
        String topicProducer = "demo_pzsp2_result";
        var objectMapper = new ObjectMapper();
//        https://www.conduktor.io/kafka/complete-kafka-consumer-with-java/#Create-Kafka-Consumer-Properties-9
        try (KafkaConsumer<String, Calculation> consumer = new KafkaConsumer<>(createConsumerProps())) {
            try (KafkaProducer<String, CalculationResult> producer = new KafkaProducer<>(createProducerProps())) {
                consumer.subscribe(List.of(topic));
                while (true) {
                    ConsumerRecords<String, Calculation> records =
                            consumer.poll(Duration.ofMillis(100));

                    for (ConsumerRecord<String, Calculation> record : records) {
                        try {
                            log.info("Consumed {}", record.value());
                            var result = record.value();
                            log.info("Produced {}", CalculationResult.from(result));
                            producer.send(new ProducerRecord<>(topicProducer, String.valueOf(result.hashCode()), CalculationResult.from(result)));
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }

                    }
                }
            }
        }

    }


}