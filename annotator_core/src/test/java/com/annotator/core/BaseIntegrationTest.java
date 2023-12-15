package com.annotator.core;

import com.annotator.core.domain.Calculation;
import com.annotator.core.repository.AnnotationsRepository;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class BaseIntegrationTest {
    @ClassRule
    public static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
    );

    @ClassRule
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    static {
        kafka.start();
        postgres.start();
        System.getProperties().setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
        System.getProperties().setProperty("spring.kafka.producer.bootstrap-servers", kafka.getBootstrapServers());
        System.getProperties().setProperty("spring.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.getProperties().setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.getProperties().setProperty("spring.datasource.username", postgres.getUsername());
        System.getProperties().setProperty("spring.datasource.password", postgres.getPassword());
    }

    @Value("${local.server.port}")
    protected int PORT;


    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    protected AnnotationsRepository annotationsRepository;
    @Autowired
    protected RestTemplate restTemplate;

    @BeforeAll
    public static void initKafka() {
        var topics = List.of(
                new NewTopic("demo_pzsp2", 1, (short) 1),
                new NewTopic("demo_pzsp2_result", 1, (short) 1)
        );
        try (var admin = AdminClient.create(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers()))) {
            admin.createTopics(topics);
        }
    }

    protected ResponseEntity<Calculation> postCalculation(Calculation calculation) {
        var uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .port(PORT)
                .host("localhost")
                .path("/calculate")
                .build().toUri();

        return restTemplate.postForEntity(uri, new HttpEntity<>(calculation), Calculation.class);
    }


}
