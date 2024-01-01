package com.annotator.core.infrastructure.persistance.annotation;

import com.annotator.core.domain.annotation.Annotation;
import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.AnnotationId;
import com.annotator.core.domain.annotation.VariantAnnotations;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "annotations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class JpaAnnotation {

    //TODO there should be also id from Order, for fast retrieval
    @EmbeddedId
    private JpaVariant variant;

    private UUID annotationId;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> results;

    private boolean state;

    public VariantAnnotations toAnnotation() {
        final var annotations = results.entrySet().stream().map(
                entry -> new Annotation(AnnotationAlgorithm.valueOf(entry.getKey()), entry.getValue())
        ).toList();
        return new VariantAnnotations(new AnnotationId(annotationId), variant.toVariant(), annotations);
    }

}
