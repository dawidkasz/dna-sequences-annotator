package com.annotator.core.infrastructure.persistance.annotation;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaVariant implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private JpaVariantDetails variant;

    public JpaVariant(final JpaVariantDetails variant) {
        this.variant = variant;
    }
}
