package com.annotator.core.application;

import com.annotator.core.domain.Allele;
import com.annotator.core.domain.Variant;

import java.util.Optional;

public interface VariantRepository {
    void save(Variant variant);

    Optional<Variant> find(String chromosome, long position, Allele refAllele, Allele altAllele);
}
