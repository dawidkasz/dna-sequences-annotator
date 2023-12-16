package com.annotator;


import com.annotator.domain.AnnotatedResult;
import com.annotator.domain.Annotation;
import com.annotator.kafka.AnnotationResultSerializer;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    static Properties createConsumerProps() {
        String bootstrapServers = "kafka:9092";
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
        String bootstrapServers = "kafka:9092";
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
        UUID uuid = UUID.randomUUID();

//        https://www.conduktor.io/kafka/complete-kafka-consumer-with-java/#Create-Kafka-Consumer-Properties-9
//        try (KafkaConsumer<String, Annotation> consumer = new KafkaConsumer<>(createConsumerProps())) {
//            try (KafkaProducer<String, AnnotatedResult> producer = new KafkaProducer<>(createProducerProps())) {
//                consumer.subscribe(List.of(topic));
//                while (true) {
//                    ConsumerRecords<String, Annotation> records =
//                            consumer.poll(Duration.ofMillis(100));
//
//                    for (ConsumerRecord<String, Annotation> record : records) {
//                        try {
//                            log.info("Consumed {}", record.value().algorithmName());
//                            String inputPath = System.getProperty("user.dir") + "/input/" + uuid.toString();
//
//                            try {
//                                Files.write(Paths.get(inputPath), record.value().variant().value().getBytes());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            log.info("Saved to a file named {}", inputPath);
//
//
//                            log.info("Starting annotation process");
//                            PythonProcessRunner runner = new PythonProcessRunner();
//                            Process p = runner.runPangolinProcess("brca.csv", "brca.annotated.pangolin");
//
//                            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                            StringBuilder output = new StringBuilder();
//                            String line;
//                            while ((line = reader.readLine()) != null) {
//                                output.append(line).append("\n");
//                            }
//
//                            // Wait for the Python process to complete
//                            int exitCode = p.waitFor();
//                            if (exitCode == 0) {
//                                log.info("Annotation process finished correctly. Sending to kafka");
//                                String filePath = System.getProperty("user.dir") + "/output/brca.annotated.pangolin.csv";
//                                String annotatedResult = new String(Files.readAllBytes(Paths.get(filePath)));
//                                log.info("Cooked {}", annotatedResult);
//
//                                producer.send(new ProducerRecord<>(topicProducer, String.valueOf(annotatedResult.hashCode()), AnnotatedResult.from(record.value(), annotatedResult)));
//                            } else {
//                                log.error("Annotation process finished with error({}): {}", exitCode, output.toString());
//                            }
//                        } catch (Exception e) {
//                            System.err.println(e.getMessage());
//                        }
//                    }
//                }
//            }
//        }
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
                            // start process and log it
                            PythonProcessRunner runner = new PythonProcessRunner();
                            Process p = runner.runPangolinProcess("brca.csv", "brca.annotated.pangolin");

                            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                            StringBuilder output = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                output.append(line).append("\n");
                            }
                            int exitCode = p.waitFor();
                            if (exitCode == 0) {
                                String filePath = System.getProperty("user.dir") + "/output/brca.annotated.pangolin.csv";
                                String annotatedResult = getAnnotatedResult(filePath);

                                log.info("Finished annotating: {}", annotatedResult);
                                producer.send(new ProducerRecord<>(topicProducer, String.valueOf(result.hashCode()), CalculationResult.from(result)));
                            }
                            else {
                                log.error("Annotation process finished with error({}): {}", exitCode, output.toString());
                            }
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }

                    }
                }
            }
        }
    }

    private static String getAnnotatedResult(String filePath) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            int totalLines = allLines.size();
            return allLines.stream()
                    .skip(Math.max(0, totalLines - 10))
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}