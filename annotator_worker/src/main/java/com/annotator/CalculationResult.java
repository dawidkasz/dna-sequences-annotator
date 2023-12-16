package com.annotator;

public record CalculationResult(int a, int b, int result) {
    public static CalculationResult from(Calculation calculation) {
        return new CalculationResult(calculation.a(), calculation.b(),
                calculation.a() + calculation.b());
    }
}
