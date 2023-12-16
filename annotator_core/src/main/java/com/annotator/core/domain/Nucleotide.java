package com.annotator.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Nucleotide {
    A("A"),
    C("C"),
    G("G"),
    T("T"),
    BLANK(".");

    @Getter
    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
