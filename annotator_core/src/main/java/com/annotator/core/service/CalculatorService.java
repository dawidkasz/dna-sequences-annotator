package com.annotator.core.service;

import com.annotator.core.domain.AnnotationResult;
import com.annotator.core.domain.Calculation;
import com.annotator.core.kafka.CalculationProducer;
import com.annotator.core.repository.AnnotationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CalculatorService {
    private final CalculationProducer calculationProducer;
    private final AnnotationsRepository annotationsRepository;

    public void requestCalculation(Calculation calculation) {
        try {
            calculationProducer.send(calculation)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AnnotationResult> listCalculations() {
        List<AnnotationResult> results = new ArrayList<>();
        annotationsRepository.findAll().forEach(results::add);
        return results;
    }
}
