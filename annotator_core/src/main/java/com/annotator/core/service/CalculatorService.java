package com.annotator.core.service;

import com.annotator.core.domain.Calculation;
import com.annotator.core.kafka.CalculationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class CalculatorService {
    private final CalculationProducer calculationProducer;

    public void requestCalculation(Calculation calculation) {
        try {
            calculationProducer.send(calculation)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
