package com.annotator.core.domain.order;

import com.annotator.core.domain.annotation.AnnotationAlgorithm;
import com.annotator.core.domain.annotation.Variant;
import com.annotator.core.domain.order.request.AnnotationRequestId;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Data
@Builder
@Getter
public class Order {
    private final OrderId orderId;
    private final OrderPositions orderPositions;

    @Builder.Default
    @Setter
    private Set<AnnotationRequestId> requestIds = new HashSet<>();

    public boolean isFinished() {
        return requestIds.isEmpty();
    }

    public Stream<OrderPosition> getPositions() {
        return orderPositions.getPositions();
    }

    public List<Variant> getVariants() {
        return orderPositions.variants();
    }

    public List<AnnotationAlgorithm> getAlgorithms() {
        return orderPositions.algorithms();
    }

    public void acceptResult(final AnnotationRequestId requestId) {
        requestIds.remove(requestId);
    }
}
