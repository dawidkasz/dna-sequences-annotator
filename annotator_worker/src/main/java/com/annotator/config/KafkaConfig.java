package com.annotator.config;

import com.annotator.domain.AnnotatedResult;
import com.annotator.domain.AnnotationRequest;
import com.annotator.kafka.AnnotatedResultSerializer;
import com.annotator.kafka.AnnotationRequestDeserializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConfig {
    public static Properties createConsumerProps() {
        final String bootstrapServers = "kafka:9092";
        final String groupId = "annotator-workers";
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AnnotationRequestDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
       properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");
//        properties.setProperty(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "test-1");
        return properties;
    }

    public static ReceiverOptions<String, AnnotationRequest> getOps() {
        return ReceiverOptions.<String, AnnotationRequest>create(createConsumerProps())
                .subscription(List.of("annotation-request"));
    }

    public static SenderOptions<String, AnnotatedResult> getSenderOps() {
        return SenderOptions.<String, AnnotatedResult>create(createProducerProps())
                .maxInFlight(1024);
    }

    public static Properties createProducerProps() {
        final String bootstrapServers = "kafka:9092";
        final Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AnnotatedResultSerializer.class.getName());
//        properties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "producer-1");

        return properties;
    }
}
