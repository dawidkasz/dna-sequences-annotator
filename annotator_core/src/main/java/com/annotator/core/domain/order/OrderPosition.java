package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.Variant;

public record OrderPosition(Variant variant, AnnotationAlgorithm algorithm) {

}
