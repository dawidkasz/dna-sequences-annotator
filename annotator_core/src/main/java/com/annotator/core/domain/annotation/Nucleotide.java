package com.annotator.core.domain.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Nucleotide {
    A("A"),
    C("C"),
    G("G"),
    T("T"),
    BLANK(".");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
