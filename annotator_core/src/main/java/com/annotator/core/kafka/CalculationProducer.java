package com.annotator.core.kafka;

import com.annotator.core.domain.Calculation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Component
@Slf4j
public class CalculationProducer {

    private final KafkaTemplate<String, Calculation> template;
    @Value("${spring.kafka.calulcation-topic:demo_pzsp2}")
    private String calculationTopic;

    //    @Scheduled(cron = "*/1 * * * * *")
    public void scheduled_send() {
        try {
            log.info("Sending records");
            String key = "a";
            Calculation c = new Calculation(1, 2);
            template.send(calculationTopic, key, c).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    public CompletableFuture send(Calculation calculation) {
        log.info("Sending record on topic {} value: {}", calculationTopic, calculation);
        return template.send(calculationTopic, String.valueOf(calculation.hashCode()), calculation);
    }

}
