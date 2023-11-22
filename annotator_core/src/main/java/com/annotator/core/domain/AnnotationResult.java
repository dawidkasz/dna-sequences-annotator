package com.annotator.core.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "results")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class AnnotationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Integer a;
    @NonNull
    private Integer b;
    @NonNull
    private Integer result;

    public static AnnotationResult from(CalculationResult calculation) {
        return new AnnotationResult(calculation.a(), calculation.b(), calculation.result());
    }

}
