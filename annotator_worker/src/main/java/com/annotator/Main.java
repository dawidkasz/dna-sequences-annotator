package com.annotator;


import com.annotator.application.AnnotationHandler;
import com.annotator.kafka.KafkaRequestConsumer;
import lombok.extern.slf4j.Slf4j;
import reactor.kafka.sender.KafkaSender;

import static com.annotator.config.KafkaConfig.getOps;
import static com.annotator.config.KafkaConfig.getSenderOps;

@Slf4j
public class Main {
    //TODO add java properties
    public static void main(final String[] args) {
        final var consumer = new KafkaRequestConsumer(getOps());
        final var producer = KafkaSender.create(getSenderOps());
        final AnnotationHandler dispatcher = new AnnotationHandler(consumer, producer, "annotation-result");
        dispatcher.start();
    }
}