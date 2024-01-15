package com.annotator.application;

import com.annotator.application.algorithm.SPIP.SPIPAlgorithm;
import com.annotator.application.algorithm.SPIP.SPIPInput;
import com.annotator.application.algorithm.pangolin.PangolinAlgorithm;
import com.annotator.application.algorithm.pangolin.PangolinInput;
import com.annotator.domain.AnnotatedResult;
import com.annotator.domain.AnnotationAlgorithm;
import com.annotator.domain.AnnotationRequest;
import com.annotator.kafka.KafkaRequestConsumer;
import com.google.common.collect.Streams;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
public class AnnotationHandler {
    private final KafkaRequestConsumer consumer;

    private final KafkaSender<String, AnnotatedResult> producer;
    private final String resultTopic;

    private static List<String> handle2(final AnnotationAlgorithm algorithm, final List<AnnotationRequest> requests) {
        switch (algorithm) {
            case PANGOLIN -> {
                return new PangolinAlgorithm().handle(requests.stream().map(PangolinInput::from).toList());
            }
            case SPIP -> {
                return new SPIPAlgorithm().handle(requests.stream().map(SPIPInput::from).toList());
            }
        }
        return List.of();
    }

    private static AnnotatedResult toResult(final AnnotationRequest request, final String result) {
        return new AnnotatedResult(
                request.getAnnotationRequestId(),
                request.getOrderId(),
                request.getAnnotationId(),
                request.getAlgorithm().name(),
                result
        );
    }

    public void start() {
        final var sendRecords = consumer.getRequestStream()
                .groupBy(record -> record.value().getAlgorithm())
                .flatMap(group -> group.bufferTimeout(1, Duration.ofSeconds(10)).map(bufferedGroup -> processGroup(group.key(), bufferedGroup)))
                .flatMap(Flux::fromStream)
                .map(this::getSenderRecord);

        producer.send(sendRecords)
                .doOnNext(r -> {
                    r.correlationMetadata().acknowledge();
                    log.debug("Sent {}", r.recordMetadata());
                })
                .subscribe();
    }

    private Stream<Tuple2<AnnotatedResult, ReceiverOffset>> processGroup(final AnnotationAlgorithm algorithm, final List<ReceiverRecord<String, AnnotationRequest>> records) {
        final var requests = records.stream().map(ReceiverRecord::value).toList();
        final var results = handle2(algorithm, requests);
        if (requests.size() != results.size()) {
            log.error("Size of results is different than requests, batch processing for {} failed", algorithm);
            return Stream.empty();
        }
        return Streams.zip(records.stream(), results.stream(),
                (record, result) -> Tuples.of(toResult(record.value(), result), record.receiverOffset()));
    }

    private SenderRecord<String, AnnotatedResult, ReceiverOffset> getSenderRecord(final Tuple2<AnnotatedResult, ReceiverOffset> receiverRecord) {
        final var value = receiverRecord.getT1();
        final ProducerRecord<String, AnnotatedResult> producerRecord = new ProducerRecord<>(
                resultTopic, value.annotationId().toString(), value
        );
        return SenderRecord.create(producerRecord, receiverRecord.getT2());
    }


}
