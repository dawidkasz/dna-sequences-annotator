package com.annotator.kafka;

import com.annotator.domain.AnnotationRequest;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Getter
public class KafkaRequestConsumer {
    private final Flux<ReceiverRecord<String, AnnotationRequest>> requestStream;

    public KafkaRequestConsumer(final ReceiverOptions<String, AnnotationRequest> options) {
        requestStream = KafkaReceiver.create(options).receive();
    }

}
