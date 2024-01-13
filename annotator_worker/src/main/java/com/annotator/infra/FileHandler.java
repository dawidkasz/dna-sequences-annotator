package com.annotator.infra;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface FileHandler {
    public Optional<Path> saveBean(final Path path, final List<CsvBean> sampleData);
}
