package com.annotator.core.infrastructure.persistance.annotation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "annotations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaAnnotation {
    @Id
    @GeneratedValue
    Long id;

    @Column
    private String batchId;

    @Column
    private Long variantId;

    @Column
    private String algorithm;

    @Column
    private String result;

}
