package com.annotator.core.domain.annotation;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public class Allele {
    private final List<Nucleotide> nucleotides;

    public Allele() {
        this.nucleotides = List.of(Nucleotide.BLANK);
    }

    public Allele(final List<Nucleotide> nucleotides) {
        checkArgument(!nucleotides.isEmpty(), "Allele can not be empty.");
        checkArgument(
                !(nucleotides.size() > 1 && nucleotides.contains(Nucleotide.BLANK)),
                "Allele can not contain any more nucleotides if it contains a blank one."
        );

        this.nucleotides = nucleotides;
    }

    public static Allele from(final String nucleotides) {
        final var parsedNucleotides = nucleotides.chars()
                .mapToObj(c -> {
                    if (c == '-') {
                        return Nucleotide.BLANK;
                    }

                    return Nucleotide.valueOf(String.valueOf((char) c));
                })
                .collect(Collectors.toList());

        return new Allele(parsedNucleotides);
    }

    public boolean isBlank() {
        return nucleotides.size() == 1 && nucleotides.get(0).equals(Nucleotide.BLANK);
    }

    public int size() {
        return nucleotides.size();
    }

    @Override
    @JsonValue
    public String toString() {
        if (isBlank()) {
            return Nucleotide.BLANK.getValue();
        }
        return nucleotides.stream().map(Nucleotide::name).collect(Collectors.joining(""));
    }
}
