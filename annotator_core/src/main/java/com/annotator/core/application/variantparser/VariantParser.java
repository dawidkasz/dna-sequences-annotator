package com.annotator.core.application.variantparser;

import com.annotator.core.domain.annotation.Variant;

import java.io.InputStream;
import java.util.List;

public interface VariantParser {
    List<Variant> read(InputStream stream);
}
