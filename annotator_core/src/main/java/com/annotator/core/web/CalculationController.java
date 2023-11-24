package com.annotator.core.web;

import com.annotator.core.domain.AnnotationResult;
import com.annotator.core.domain.Calculation;
import com.annotator.core.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalculationController {
    private final CalculatorService calculatorService;

    @PostMapping("/calculate")
    public void receiveCalculation(@RequestBody Calculation calculation) {
        calculatorService.requestCalculation(calculation);
    }

    @GetMapping("/list")
    public List<AnnotationResult> listCalculations() {
        return calculatorService.listCalculations();
    }
}
