package com.annotator.application;

import com.annotator.application.algorithm.pangolin.PangolinAlgorithm;
import com.annotator.application.algorithm.pangolin.PangolinInput;
import com.annotator.domain.AnnotatedResult;
import com.annotator.domain.AnnotationRequest;
import com.annotator.kafka.KafkaRequestConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class AnnotationDispatcher {
    private final KafkaRequestConsumer consumer;

    private final KafkaSender<String, AnnotatedResult> producer;
    private final String resultTopic;

    private static Optional<String> handle(final AnnotationRequest request) {
        switch (request.getAlgorithm()) {
            case PANGOLIN -> {
                return new PangolinAlgorithm().handle(List.of(PangolinInput.from(request)));
            }
        }
        return Optional.empty();
    }

    public void start() {
        final var sendRecords = consumer.getRequestStream()
                .flatMap(this::processRequest)
                .map(this::getSenderRecord);

        producer.send(sendRecords)
                .doOnNext(r -> {
                    r.correlationMetadata().acknowledge();
                    log.info("Sent {}", r.recordMetadata());
                })
                .subscribe();
    }

    private Mono<Tuple2<AnnotatedResult, ReceiverOffset>> processRequest(final ReceiverRecord<String, AnnotationRequest> r) {
        final var request = r.value();
        return handle(request)
                .map(result -> Mono.just(
                        Tuples.of(new AnnotatedResult(request.getAnnotationId(), result), r.receiverOffset()))
                )
                .orElse(Mono.empty());
    }

    private SenderRecord<String, AnnotatedResult, ReceiverOffset> getSenderRecord(final Tuple2<AnnotatedResult, ReceiverOffset> receiverRecord) {
        final var value = receiverRecord.getT1();
        final ProducerRecord<String, AnnotatedResult> producerRecord = new ProducerRecord<>(
                resultTopic, value.annotationId().getUuid().toString(), value
        );
        return SenderRecord.create(producerRecord, receiverRecord.getT2());
    }


}
