package com.annotator;


import com.annotator.application.AnnotationDispatcher;
import com.annotator.kafka.KafkaRequestConsumer;
import lombok.extern.slf4j.Slf4j;
import reactor.kafka.sender.KafkaSender;

import static com.annotator.config.KafkaConfig.getOps;
import static com.annotator.config.KafkaConfig.getSenderOps;

@Slf4j
public class Main {

    public static void main(final String[] args) {
        final var consumer = new KafkaRequestConsumer(getOps());
        final var producer = KafkaSender.create(getSenderOps());
        final AnnotationDispatcher dispatcher = new AnnotationDispatcher(consumer, producer, "annotation-result");
        dispatcher.start();
    }
}