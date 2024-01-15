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
    BLANK("-");

    private final String value;

    public static Nucleotide from(final String value) {
        switch (value) {
            case "A" -> {
                return Nucleotide.A;
            }
            case "C" -> {
                return Nucleotide.C;
            }
            case "G" -> {
                return Nucleotide.G;
            }
            case "T" -> {
                return Nucleotide.T;
            }
        }
        return Nucleotide.BLANK;
    }

    @Override
    public String toString() {
        return value;
    }
}
