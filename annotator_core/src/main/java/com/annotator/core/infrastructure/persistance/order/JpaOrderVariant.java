package com.annotator.core.infrastructure.persistance.order;

import com.annotator.core.infrastructure.persistance.annotation.JpaVariant;
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
public class JpaOrderVariant implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private JpaVariant variant;

    public JpaOrderVariant(final JpaVariant variant) {
        this.variant = variant;
    }
}
